package org.example;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class Game extends Pane {
    private ArrayList<Sprite> wallsSprites = new ArrayList<>();
    private ArrayList<Sprite> coinsSprites = new ArrayList<>();
    private ArrayList<Sprite> ghostsSprites = new ArrayList<>();

    Sprite hero;
    private final double SPEED_HERO = 5;

    int score = 0;
    int lives = 3;
    int level = 1;

    int coinsLeft;

    Label scoreLabel = new Label("Score : " + score);
    Label livesLabel = new Label("Lives left : " + lives);
    Label levelLabel = new Label("Level : " + level);

    public Game(){
        super();

        scoreLabel.setFont(new Font("Vegenta", 15));
        scoreLabel.setLayoutX(50);
        scoreLabel.setLayoutY(15);

        livesLabel.setFont(new Font("Vegenta", 15));
        livesLabel.setLayoutX(170);
        livesLabel.setLayoutY(15);

        levelLabel.setFont(new Font("Vegenta", 15));
        levelLabel.setLayoutX(300);
        levelLabel.setLayoutY(15);

        newGame();

        AnimationTimer timer = new AnimationTimer() {
            double time = 0.25;

            @Override
            public void handle(long l) {
                time -= 0.01;
                if(time < 0)
                {
                    time = 0.25;

                    for (int i = 0; i < ghostsSprites.size(); i++) {
                        if(ghostsSprites.get(i).isIntersects(hero)){
                            lives--;
                            livesLabel.setText("Lives : " + lives);
                           if(lives >= 1){
                               stop();
                               endGame();
                               start();
                           }else{
                               stop();

                               score = 0;
                               level = 1;
                               lives = 3;

                               levelLabel.setText("Level : " + level);
                               scoreLabel.setText("score : " + score);
                               livesLabel.setText("Lives left : " + lives);

                               endGame();
                               start();
                           }


                        }
                    }

                    for (var s : ghostsSprites) {
                    s.ghostMove(s, wallsSprites);
                }
                    start();
                }
            }
        };
        timer.start();

        setOnKeyPressed(e->{
            double speedX = 0;
            double speedY = 0;

            switch (e.getCode()){
                case UP:
                   speedY = -SPEED_HERO;
                   hero.setRotate(-90);
                    break;
                case DOWN:
                    speedY = SPEED_HERO;
                    hero.setRotate(90);
                    break;
                case LEFT:
                    speedX = -SPEED_HERO;
                    hero.setRotate(-180);
                    break;
                case RIGHT:
                    speedX = SPEED_HERO;
                    hero.setRotate(0);
                    break;
            }

            hero.heroMove(speedX, speedY);


            for (var s: wallsSprites) {
                if(s.isIntersects(hero))
                    hero.heroMove(-speedX, -speedY);
            }

            for (int i = 0; i < coinsSprites.size(); i++) {
                if(coinsSprites.get(i).isCoinGot(hero) && getChildren().contains(coinsSprites.get(i))){
                    getChildren().remove(coinsSprites.get(i));

                    score += 10;
                    scoreLabel.setText("Score : " + score);

                    coinsLeft--;
                    if(coinsLeft == 0){
                        level++;
                        levelLabel.setText("Level : " + level);

                        endGame();
                    }
            }
            }

        });

        setFocusTraversable(true);
        setFocused(true);
    }

    public void endGame(){
        getChildren().clear();
        ghostsSprites.clear();
        coinsSprites.clear();
        wallsSprites.clear();

        newGame();
    }


    public void newGame(){
        int[][] buffMap = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 0, 0, 0, 0, 0, 3, 3, 3, 0, 1},
                {1, 0, 1, 1, 1, 1, 4, 1, 1, 1, 0, 1},
                {1, 3, 3, 3, 3, 3, 0, 1, 3, 3, 3, 1},
                {1, 0, 1, 1, 1, 1, 0, 1, 3, 1, 3, 1},
                {1, 0, 1, 3, 3, 3, 3, 1, 0, 1, 3, 1},
                {1, 3, 1, 0, 1, 4, 1, 1, 0, 1, 0, 1},
                {1, 3, 1, 0, 1, 3, 3, 1, 0, 4, 0, 1},
                {1, 3, 3, 3, 3, 1, 3, 3, 0, 1, 0, 1},
                {1, 0, 1, 1, 4, 1, 1, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 3, 3, 3, 3, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };

        int ghostNum = 0;

        for (int i = 0; i < buffMap.length; i++) {
            for (int j = 0; j < buffMap[0].length; j++) {
                if(buffMap[i][j] == 1){
                    wallsSprites.add(new Sprite("Wall.png", 50*j, 50*i, 50,50));
                }else if(buffMap[i][j] == 2){
                    hero = new Sprite("PacMan1.png", 50*j, 50*i, 50,50);
                }else if(buffMap[i][j] == 3){
                    coinsSprites.add(new Sprite("Coin.png", 50*j, 50*i, 50, 50));
                }else if(buffMap[i][j] == 4){
                    ghostsSprites.add(new Sprite("Ghost" + (++ghostNum) + ".png", 50*j, 50*i, 50, 50));
                }
            }
        }

        for(int i=0;i<wallsSprites.size();i++)
            getChildren().add(wallsSprites.get(i));

        getChildren().add(hero);

        for(int i=0;i<coinsSprites.size();i++){
            getChildren().add(coinsSprites.get(i));
        }
        coinsLeft =coinsSprites.size();

        for(int i=0;i<ghostsSprites.size();i++)
            getChildren().add(ghostsSprites.get(i));

        getChildren().addAll(scoreLabel, livesLabel, levelLabel);
    }



}
