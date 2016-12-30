package bots.ThemanuBot.componets;

import bots.ThemanuBot.Component;

public class AllComponents {
	private final Component[] components = new Component[] {
			//add Component here:
			new General(),
			
	};
	
	public AllComponents () {
		
	}
	
	public Component[] getComponents() {
		return components;
	}
}
