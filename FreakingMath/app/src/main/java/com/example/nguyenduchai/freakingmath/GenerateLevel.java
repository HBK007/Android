package com.example.nguyenduchai.freakingmath;

import java.util.Random;

/**
 * Created by Nguyen Duc Hai on 3/21/2017.
 */

public class GenerateLevel {
    // score in level
    public static final int SCORE_EASY = 10;
    public static final int SCORE_MEDIUM = 20;
    public static final int SCORE_HARD = 150;

    public static LevelModel generateLevel(int count) {
        LevelModel level = new LevelModel();
        Random rand = new Random();

        if (count <= SCORE_EASY) {
            level.difficultLevel = 1;

        } else {
            if (count <= SCORE_MEDIUM) {
                level.difficultLevel = 2;
            } else {
                if (count <= SCORE_HARD) {
                    level.difficultLevel = 3;
                }else{
                    level.difficultLevel = 4;
                }
            }
        }
        // random operation
        level.operator = rand.nextInt(level.difficultLevel);

        // random operator
        level.x = rand.nextInt(level.arrMaxOperatorValue[level.difficultLevel - 1]);
        level.y = rand.nextInt(level.arrMaxOperatorValue[level.difficultLevel - 1]);

        // random result : correct or wrong
        level.correctWrong = rand.nextBoolean();

        // random result
        if(level.correctWrong == false){
            switch(level.operator){
                case LevelModel.ADD:
                    do{
                        level.result = rand.nextInt(level.arrMaxOperatorValue[level.difficultLevel - 1]);
                    }while(level.result == level.x + level.y);
                    break;
                case LevelModel.SUB:
                    do{
                        level.result = rand.nextInt(level.arrMaxOperatorValue[level.difficultLevel - 1]);
                    }while(level.result == level.x - level.y);
                    break;
                case LevelModel.MUL:
                    do{
                        level.result = rand.nextInt(level.arrMaxOperatorValue[level.difficultLevel - 1]);
                    }while(level.result == level.x * level.y);

                    break;
                case LevelModel.DIV:
                    do{
                        level.result = rand.nextInt(level.arrMaxOperatorValue[level.difficultLevel - 1]);
                    }while(level.result == level.x / level.y);
                    break;
                default:
                    break;
            }
        }else{
            switch (level.operator){
                case LevelModel.ADD:
                    level.result = level.x + level.y;
                    break;
                case LevelModel.SUB:
                    level.result = level.x - level.y;
                    break;
                case LevelModel.MUL:
                    level.result = level.x * level.y;
                    break;
                case LevelModel.DIV:
                    level.result = level.x / level.y;
                    break;
                default:
                    break;
            }
        }
        // create string display on screen
        // create string operator
        level.strOperator = String.valueOf(level.x) + level.arrOperatorText[level.operator] +
                            String.valueOf(level.y);
        // create string result
        level.strResult = LevelModel.EQU_TEXT + String.valueOf(level.result);

        // return Level for next new level
        return level;
    }
}