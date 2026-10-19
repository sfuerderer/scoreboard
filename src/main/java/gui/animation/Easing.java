/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package gui.animation;

import javafx.animation.Interpolator;

/**
 * Easing curves for the scoreboard animations. Movements start fast and slow down softly, which
 * looks more natural than the linear default.
 */
public final class Easing {
	public static final Interpolator EASE_OUT = Interpolator.SPLINE(0.16, 1, 0.3, 1);
	public static final Interpolator EASE_IN = Interpolator.SPLINE(0.7, 0, 0.84, 0);

	/**
	 * Slows down like {@link #EASE_OUT} but shoots a little beyond the target first.
	 * Interpolator.SPLINE only accepts control points between 0 and 1, so the curve is calculated here.
	 */
	public static final Interpolator EASE_OUT_BACK = new Interpolator() {
		private static final double D_OVERSHOOT = 1.2;

		@Override
		protected double curve(double t) {
			double dRest = t - 1;
			return 1 + (D_OVERSHOOT + 1) * dRest * dRest * dRest + D_OVERSHOOT * dRest * dRest;
		}
	};

	private Easing() {
	}
}
