/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package football.person;

import language.Language;

public enum CoachType {
	HEADCOACH(0) {
		@Override
		public String toString() {
			return Language.getText("trainer");
		}
	},
	ASSISTENTCOACH(1) {
		@Override
		public String toString() {
			return Language.getText("co_trainer");
		}
	},
	GOALKEEPERCOACH(2) {
		@Override
		public String toString() {
			return Language.getText("torwart_trainer");
		}
	};

	private int iId;

	CoachType(int iId) {
		this.iId = iId;
	}

	public int getIId() {
		return iId;
	}
}
