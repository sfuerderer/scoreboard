// Copyright (c) 2026 Sebastian Fürderer
// All rights reserved
//
// Stellt Spielerfotos frei (transparenter Hintergrund) für die Anzeigetafel.
// Nutzt die Motiv-Erkennung von macOS (Vision, ab macOS 14), dieselbe wie „Motiv kopieren“ in Fotos.
//
// Aufruf: freistellen <Foto oder Ordner> <Ausgabeordner>
//
// Jedes Foto wird als PNG mit gleichem Namen in den Ausgabeordner geschrieben. Der Zuschnitt ist für alle
// Fotos gleich: oben ein kleiner fester Rand über dem Kopf, unten bleibt das Foto wie es ist, damit die Köpfe
// auf der Anzeigetafel auf derselben Höhe stehen. Die Originale werden nicht verändert.

import CoreImage
import Foundation
import Vision

// Breite der fertigen PNGs; reicht für die 280 Pixel hohe Wand mit Reserve
let maxWidth: CGFloat = 800
// Rand über dem Kopf als Anteil der Bildhöhe
let topMargin: CGFloat = 0.03
// Ab diesem Wert gehört ein Pixel der Maske zum Motiv
let maskThreshold: Float = 0.5
let imageExtensions: Set<String> = ["jpg", "jpeg", "png", "heic"]

func printError(_ message: String) {
    FileHandle.standardError.write((message + "\n").data(using: .utf8)!)
}

enum Result {
    case done
    case noSubject
    case failed(String)
}

@available(macOS 14.0, *)
func cutOut(_ input: URL, to output: URL, context: CIContext) -> Result {
    guard let image = CIImage(contentsOf: input, options: [.applyOrientationProperty: true]) else {
        return .failed("Bild lässt sich nicht lesen")
    }
    let request = VNGenerateForegroundInstanceMaskRequest()
    let handler = VNImageRequestHandler(ciImage: image)
    do {
        try handler.perform([request])
        guard let observation = request.results?.first, !observation.allInstances.isEmpty else {
            return .noSubject
        }
        let masked = try observation.generateMaskedImage(
            ofInstances: observation.allInstances, from: handler, croppedToInstancesExtent: false)
        let mask = try observation.generateScaledMaskForImage(
            forInstances: observation.allInstances, from: handler)
        guard let subjectTop = topRow(of: mask) else {
            return .noSubject
        }

        let cutOut = CIImage(cvPixelBuffer: masked)
        let width = cutOut.extent.width
        let height = cutOut.extent.height
        // Core Image zählt y von unten, die Maske von oben
        let top = max(0, CGFloat(subjectTop) - topMargin * height)
        let cropped = cutOut.cropped(to: CGRect(x: 0, y: 0, width: width, height: height - top))

        let scale = min(1, maxWidth / width)
        let scaled = scale < 1 ? scaleImage(cropped, by: scale) : cropped
        let colorSpace = CGColorSpace(name: CGColorSpace.sRGB)!
        try context.writePNGRepresentation(of: scaled, to: output, format: .RGBA8, colorSpace: colorSpace)
        return .done
    } catch {
        return .failed(error.localizedDescription)
    }
}

/// Oberste Zeile der Maske, in der ein Pixel zum Motiv gehört
func topRow(of mask: CVPixelBuffer) -> Int? {
    CVPixelBufferLockBaseAddress(mask, .readOnly)
    defer { CVPixelBufferUnlockBaseAddress(mask, .readOnly) }
    guard let base = CVPixelBufferGetBaseAddress(mask) else { return nil }
    let width = CVPixelBufferGetWidth(mask)
    let height = CVPixelBufferGetHeight(mask)
    let bytesPerRow = CVPixelBufferGetBytesPerRow(mask)
    for row in 0..<height {
        let pixels = (base + row * bytesPerRow).assumingMemoryBound(to: Float32.self)
        for column in 0..<width where pixels[column] > maskThreshold {
            return row
        }
    }
    return nil
}

func scaleImage(_ image: CIImage, by scale: CGFloat) -> CIImage {
    let filter = CIFilter(name: "CILanczosScaleTransform")!
    filter.setValue(image, forKey: kCIInputImageKey)
    filter.setValue(scale, forKey: kCIInputScaleKey)
    filter.setValue(1.0, forKey: kCIInputAspectRatioKey)
    return filter.outputImage!
}

func imageFiles(in input: URL) -> [URL] {
    var isDirectory: ObjCBool = false
    guard FileManager.default.fileExists(atPath: input.path, isDirectory: &isDirectory) else { return [] }
    if !isDirectory.boolValue {
        return [input]
    }
    let files = (try? FileManager.default.contentsOfDirectory(
        at: input, includingPropertiesForKeys: nil, options: [.skipsHiddenFiles])) ?? []
    return files
        .filter { imageExtensions.contains($0.pathExtension.lowercased()) }
        .sorted { $0.lastPathComponent.localizedStandardCompare($1.lastPathComponent) == .orderedAscending }
}

@available(macOS 14.0, *)
func run(_ arguments: [String]) -> Int32 {
    guard arguments.count == 3 else {
        printError("Aufruf: freistellen <Foto oder Ordner> <Ausgabeordner>")
        return 2
    }
    let input = URL(fileURLWithPath: arguments[1]).standardizedFileURL
    let outputFolder = URL(fileURLWithPath: arguments[2]).standardizedFileURL

    if !FileManager.default.fileExists(atPath: input.path) {
        printError("Nicht gefunden: \(input.path) (USB-Stick eingesteckt?)")
        return 1
    }
    let files = imageFiles(in: input)
    if files.isEmpty {
        printError("Keine Fotos gefunden in \(input.path)")
        return 1
    }
    let inputFolder = files.count == 1 && files[0] == input ? input.deletingLastPathComponent() : input
    if inputFolder.resolvingSymlinksInPath() == outputFolder.resolvingSymlinksInPath() {
        printError("Der Ausgabeordner darf nicht der Ordner mit den Originalen sein.")
        return 2
    }
    do {
        try FileManager.default.createDirectory(at: outputFolder, withIntermediateDirectories: true)
    } catch {
        printError("Ausgabeordner lässt sich nicht anlegen: \(error.localizedDescription)")
        return 1
    }

    let context = CIContext()
    var failures = 0
    for file in files {
        let output = outputFolder.appendingPathComponent(file.deletingPathExtension().lastPathComponent)
            .appendingPathExtension("png")
        switch cutOut(file, to: output, context: context) {
        case .done:
            print("✓ \(output.lastPathComponent)")
        case .noSubject:
            failures += 1
            print("✗ \(file.lastPathComponent): keine Person erkannt")
        case .failed(let reason):
            failures += 1
            print("✗ \(file.lastPathComponent): \(reason)")
        }
    }
    print("\(files.count - failures) von \(files.count) Fotos freigestellt → \(outputFolder.path)")
    return failures == 0 ? 0 : 1
}

if #available(macOS 14.0, *) {
    exit(run(CommandLine.arguments))
} else {
    printError("Freistellen braucht macOS 14 oder neuer.")
    exit(1)
}
