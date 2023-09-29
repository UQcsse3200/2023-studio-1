package com.csse3200.game.services;

public class ParticleService {

	public static final String WEATHER_EVENT = "WEATHER_EVENT";

	public enum ParticleEffects {
		ACID_RAIN(WEATHER_EVENT, "particle-effects/acid_rain.p");

		private final String category;
		private final String effectPath;

		ParticleEffects(String category, String effectPath) {
			this.category = category;
			this.effectPath = effectPath;
		}

		public String getCategory() {
			return category;
		}

		private String getEffectPath() {
			return effectPath;
		}
	}
}
