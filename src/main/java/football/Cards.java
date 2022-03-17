package football;

import language.Language;

public enum Cards {
	YELLOW {
		@Override
		public String toString() {
			return Language.getText("gelbe_karte");
		}
	},
	RED {
		@Override
		public String toString() {
			return Language.getText("rote_karte");
		}
	},
	YELLOW_RED {
		@Override
		public String toString() {
			return Language.getText("gelb-rote_karte");
		}
	}
}
