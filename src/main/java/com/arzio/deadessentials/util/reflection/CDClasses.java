package com.arzio.deadessentials.util.reflection;

import java.lang.reflect.Field;

import com.arzio.deadessentials.util.CDMaterial;
import com.arzio.deadessentials.util.reflection.ReflectionHelper.FieldChecker;
import com.arzio.deadessentials.util.reflection.finder.ContentFinder;

import net.minecraft.server.v1_6_R3.Item;

public class CDClasses {

	// ItemGun class and its fields
	public static final Class<?> ITEMGUN_CLASS = Item.byId[CDMaterial.M4A1.getId()].getClass();
	public static final IndirectField<Integer> ITEMGUN_BODY_DAMAGE_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<Integer>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], int.class, 6).build());
	public static final IndirectField<Integer> ITEMGUN_HEADSHOT_DAMAGE_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<Integer>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], int.class, 13).build());
	public static final IndirectField<int[]>   ITEMGUN_COMPATIBLE_MAGAZINES_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<int[]>().withType(int[].class).build());
	public static final IndirectField<Float>   ITEMGUN_ROUNDS_PER_MINUTE_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<Float>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], float.class, 700F).build());
	public static final IndirectField<Integer> ITEMGUN_BULLETS_PER_ROUND_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<Integer>().withExactValue(Item.byId[CDMaterial.MOSSBERG.getId()], int.class, 8).build());
	public static final IndirectField<Float>   ITEMGUN_SOUND_LEVEL_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<Float>().withExactValue(Item.byId[CDMaterial.M1_GARAND.getId()], float.class, 1.5F).build());
	public static final IndirectField<String>  ITEMGUN_SOUND_NAME_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<String>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], String.class, "m4a1shoot").build());
	public static final IndirectField<String>  ITEMGUN_SUPRESSED_SOUND_NAME_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<String>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], String.class, "m4a1shootsd").build());
	public static final IndirectField<String>  ITEMGUN_RELOAD_SOUND_NAME_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<String>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], String.class, "m4a1reload").build());
	public static final IndirectField<Integer> ITEMGUN_BULLET_DISTANCE_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<Integer>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], int.class, 120).build());
	public static final IndirectField<Float> ITEMGUN_RECOIL_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<Float>().withExactValue(Item.byId[CDMaterial.M1_GARAND.getId()], float.class, 14F).build());
	public static final IndirectField<Float> ITEMGUN_ACCURACY_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<Float>().withExactValue(Item.byId[CDMaterial.M1_GARAND.getId()], float.class, 3F).build());
	public static final IndirectField<Double> ITEMGUN_MOVEMENT_PENALTY_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<Double>().withExactValue(Item.byId[CDMaterial.M1_GARAND.getId()], double.class, 0.09D).build());
	public static final IndirectField<Float> ITEMGUN_ZOOM_LEVEL_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<Float>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], float.class, 2.0F).build());
	public static final IndirectField<Boolean> ITEMGUN_RENDER_CROSSHAIRS_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<Boolean>().withValue(Item.byId[CDMaterial.M4A1.getId()], boolean.class, new FieldChecker<Boolean>() {

		@Override
		public boolean isCorrect(Boolean found, Field field) throws Exception {
			return found && !field.getBoolean(Item.byId[CDMaterial.AWP.getId()]) && !field.getBoolean(Item.byId[CDMaterial.M107.getId()]);
		}

	}).build());
	public static final IndirectField<Boolean> ITEMGUN_SPREAD_WHILE_AIMING_FIELD = new IndirectField<>(ITEMGUN_CLASS, new ContentFinder.FieldBuilder<Boolean>().withValue(Item.byId[CDMaterial.AWP.getId()], boolean.class, new FieldChecker<Boolean>() {

		@Override
		public boolean isCorrect(Boolean found, Field field) throws Exception {
			return !found && field.getBoolean(Item.byId[CDMaterial.MOSSBERG.getId()]) && field.getBoolean(Item.byId[CDMaterial.TRENCH_GUN.getId()]);
		}

	}).build());

	// ItemCD class
	public static final Class<?> ITEMCD_CLASS = ITEMGUN_CLASS.getSuperclass();

	// ItemMagazine class and its fields
	public static final Class<?> ITEMMAGAZINE_CLASS = Item.byId[CDMaterial.M1_GARAND_MAGAZINE.getId()].getClass();
	public static final IndirectField<Double> ITEMMAGAZINE_PENETRATION_FIELD = new IndirectField<>(ITEMMAGAZINE_CLASS, new ContentFinder.FieldBuilder<Double>().withExactValue(Item.byId[CDMaterial.M1_GARAND_MAGAZINE.getId()], double.class, 95D).build());
	public static final IndirectField<Integer> ITEMMAGAZINE_BULLET_AMOUNT_FIELD = new IndirectField<>(ITEMMAGAZINE_CLASS, new ContentFinder.FieldBuilder<Integer>().withExactValue(Item.byId[CDMaterial.M1_GARAND_MAGAZINE.getId()], int.class, 8).build());
}
