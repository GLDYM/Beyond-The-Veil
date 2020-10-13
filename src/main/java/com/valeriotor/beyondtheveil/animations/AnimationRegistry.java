package com.valeriotor.beyondtheveil.animations;

public class AnimationRegistry {
	
	public static AnimationTemplate deep_one_roar;
	public static AnimationTemplate shoggoth_open_mouth;
	public static AnimationTemplate shoggoth_eye_tentacle;
	public static AnimationTemplate blood_zombie_idle;
	public static AnimationTemplate blood_skeleton_idle;
	public static AnimationTemplate blood_skeleton_spook;
	public static AnimationTemplate blood_skeleton_left_swing;
	public static AnimationTemplate blood_skeleton_right_swing;
	public static AnimationTemplate blood_zombie_left_swing;
	public static AnimationTemplate blood_zombie_right_swing;
	public static AnimationTemplate deep_one_right_swing;
	public static AnimationTemplate deep_one_left_swing;
	public static AnimationTemplate crazed_weeper_transform;
	public static AnimationTemplate surgeon_surgery;
	public static AnimationTemplate surgeon_attack;
	public static AnimationTemplate muray_bite;
	
	public static void loadAnimations() {
		blood_skeleton_left_swing = new AnimationTemplate("blood_skeleton_left_swing");
		blood_skeleton_right_swing = new AnimationTemplate("blood_skeleton_right_swing");
		blood_skeleton_idle = new AnimationTemplate("blood_skeleton_idle");
		blood_skeleton_spook = new AnimationTemplate("blood_skeleton_spook");
		blood_zombie_idle = new AnimationTemplate("blood_zombie_idle");
		deep_one_roar = new AnimationTemplate("deep_one_roar");
		shoggoth_open_mouth = new AnimationTemplate("shoggoth_open_mouth");
		shoggoth_eye_tentacle = new AnimationTemplate("shoggoth_eye_tentacle");
		deep_one_left_swing = new AnimationTemplate("deep_one_left_swing");
		deep_one_right_swing = new AnimationTemplate("deep_one_right_swing");
		crazed_weeper_transform = new AnimationTemplate("crazed_weeper_transform");
		blood_zombie_left_swing = new AnimationTemplate("blood_zombie_left_swing");
		blood_zombie_right_swing = new AnimationTemplate("blood_zombie_right_swing");
		surgeon_surgery = new AnimationTemplate("surgeon_surgery");
		surgeon_attack = new AnimationTemplate("surgeon_attack");
		muray_bite = new AnimationTemplate("muray_bite");
	}
	
	public static int getIdFromAnimation(AnimationTemplate anim) {
		if(anim == deep_one_roar) return 0;
		if(anim == shoggoth_open_mouth) return 1;
		if(anim == shoggoth_eye_tentacle) return 2;
		if(anim == blood_zombie_idle) return 3;
		if(anim == blood_skeleton_idle) return 4;
		if(anim == blood_skeleton_spook) return 5;
		if(anim == deep_one_left_swing) return 6;
		if(anim == deep_one_right_swing) return 7;
		if(anim == blood_skeleton_left_swing) return 8;
		if(anim == blood_skeleton_right_swing) return 9;
		if(anim == crazed_weeper_transform) return 10;
		if(anim == blood_zombie_left_swing) return 11;
		if(anim == blood_zombie_right_swing) return 12;
		if(anim == surgeon_surgery) return 13;
		if(anim == surgeon_attack) return 14;
		if(anim == muray_bite) return 15;
		return -1;
	}
	
	public static AnimationTemplate getAnimationFromId(int id) {
		switch(id) {
			case 0: return deep_one_roar;
			case 1: return shoggoth_open_mouth;
			case 2: return shoggoth_eye_tentacle;
			case 3: return blood_zombie_idle;
			case 4: return blood_skeleton_idle;
			case 5: return blood_skeleton_spook;
			case 6: return deep_one_left_swing;
			case 7: return deep_one_right_swing;
			case 8: return blood_skeleton_left_swing;
			case 9: return blood_skeleton_right_swing;
			case 10: return crazed_weeper_transform;
			case 11: return blood_zombie_left_swing;
			case 12: return blood_zombie_right_swing;
			case 13: return surgeon_surgery;
			case 14: return surgeon_attack;
			case 15: return muray_bite;
		}
		return null;
	}
	
}
