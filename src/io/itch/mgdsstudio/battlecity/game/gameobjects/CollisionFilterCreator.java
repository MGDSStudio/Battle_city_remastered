package io.itch.mgdsstudio.battlecity.game.gameobjects;

public abstract class CollisionFilterCreator {
    final public static int CATEGORY_GAME_OBJECT_OR_ENEMY =           0b0000_0000_0000_0001;
    final public static int CATEGORY_BULLET =                0b0000_0000_0000_0010;
    final public static int CATEGORY_WATER_OR_COLLECTABLE =  0b0000_0000_0000_0100;

    final public static int CATEGORY_MINE_FOR_PLAYER =  0b0000_0000_0000_1000;
    
    final public static int CATEGORY_PLAYER =  0b0000_0000_0010_0000;
    final public static int CATEGORY_MINE_FOR_ENEMY =  0b0000_0000_0100_0000;

    //Masks
    final private static int MASK_PLAYER = CATEGORY_GAME_OBJECT_OR_ENEMY | CATEGORY_BULLET | CATEGORY_WATER_OR_COLLECTABLE | CATEGORY_MINE_FOR_PLAYER | CATEGORY_PLAYER;
    final private static int MASK_GAME_OBJECT_OR_ENEMY = CATEGORY_GAME_OBJECT_OR_ENEMY | CATEGORY_BULLET | CATEGORY_WATER_OR_COLLECTABLE | CATEGORY_MINE_FOR_ENEMY | CATEGORY_PLAYER;
    final private static int MAST_BULLET = CATEGORY_GAME_OBJECT_OR_ENEMY | CATEGORY_BULLET | CATEGORY_PLAYER;
    final private static int MASK_WATER_OR_COLLECTABLE = CATEGORY_GAME_OBJECT_OR_ENEMY | CATEGORY_PLAYER;
    final private static int MASK_MINE_FOR_PLAYER = CATEGORY_PLAYER;
    final private static int MASK_MINE_FOR_ENEMY = CATEGORY_GAME_OBJECT_OR_ENEMY;




    public static int getMaskForCategory(int category){
        if (category == CATEGORY_GAME_OBJECT_OR_ENEMY) return MASK_GAME_OBJECT_OR_ENEMY;
        else if (category == CATEGORY_BULLET) return MAST_BULLET;
        else if (category == CATEGORY_WATER_OR_COLLECTABLE) return MASK_WATER_OR_COLLECTABLE;
        else if (category == CATEGORY_MINE_FOR_PLAYER) return MASK_MINE_FOR_PLAYER;
        else if (category == CATEGORY_MINE_FOR_ENEMY) return MASK_MINE_FOR_ENEMY;
        else if (category == CATEGORY_PLAYER) return MASK_PLAYER;
        else {
            System.out.println("No data about this category");
            return CATEGORY_GAME_OBJECT_OR_ENEMY;
        }

    }

}
