package org.example;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Sprite  extends ImageView {
    private Image image;
    double width, height;


    int[] availableDirs = new int[4];//0 - up, 1 - left, 2 - down, 3 - right
    int availableDirsCount = 0;

    int prevDir = 0;
    int opDir = 0;

    public Sprite(String fileName, double locX, double locY, double width, double height) {
        super();
        image = new Image(fileName, width, height, false, false);
        setImage(image);

        this.width = width;
        this.height = height;

        setLayoutX(locX);
        setLayoutY(locY);
    }

    public void heroMove(double speedX, double speedY) {
        setLayoutX(speedX + getLayoutX());
        setLayoutY(speedY + getLayoutY());
    }

    public boolean isIntersects(Sprite sprite) {
        double x = Math.abs(getLayoutX() - sprite.getLayoutX());
        double y = Math.abs(getLayoutY() - sprite.getLayoutY());

        return x < width && y < height;
    }

    public boolean isCoinGot(Sprite sprite) {
        double x = Math.abs(getLayoutX() - sprite.getLayoutX());
        double y = Math.abs(getLayoutY() - sprite.getLayoutY());

        return x < width - 12 && y < height - 12;
    }

    public void ghostMove(Sprite ghost, ArrayList<Sprite> wallsSprites) {


        if (ghost.getLayoutX() % 50 == 0 && ghost.getLayoutY() % 50 == 0) {
            directionsViewer(ghost, wallsSprites);
            newDir(ghost);
        }
        else
            blockMove(ghost);


    }

    private void directionsViewer(Sprite ghost, ArrayList<Sprite> wallsSprites) {
        double x = ghost.getLayoutX();
        double y = ghost.getLayoutY();


        ghost.setLayoutY(y - 10);
        for (var s : wallsSprites) {
            if (s.isIntersects(ghost)) {
                availableDirs[0] = 0;
                break;
            } else availableDirs[0] = 1;
        }

        ghost.setLayoutY(y + 20);
        for (var s : wallsSprites) {
            if (s.isIntersects(ghost)) {
                availableDirs[2] = 0;
                break;
            } else availableDirs[2] = 1;
        }
        ghost.setLayoutY(y);

        ghost.setLayoutX(x - 10);
        for (var s : wallsSprites) {
            if (s.isIntersects(ghost)) {
                availableDirs[1] = 0;
                break;
            } else availableDirs[1] = 1;
        }

        ghost.setLayoutX(x + 20);
        for (var s : wallsSprites) {
            if (s.isIntersects(ghost)) {
                availableDirs[3] = 0;
                break;
            } else availableDirs[3] = 1;
        }
        ghost.setLayoutX(x);

        for (int i = 0; i < availableDirs.length; i++) {
            if (availableDirs[i] == 1)
                availableDirsCount++;

        }
    }

    private void newDir(Sprite ghost) {
        int dir = 1;

        if(availableDirsCount == 1){
            if(prevDir == 0)
                dir = 2;
            else if(prevDir == 1)
                dir = 3;
            else if(prevDir == 2)
                dir = 0;
            else
                dir = 1;
        }
        else if (availableDirsCount == 2 && availableDirs[prevDir] == 1)
        {
            dir = prevDir;
        }
        else if(availableDirsCount > 2) {
            do {
                dir = (int) (Math.random() * 4);
            } while (availableDirs[dir] != 1 || dir == opDir);
        }


        switch (dir){
            case 0: ghost.setLayoutY(ghost.getLayoutY() - 10);
            prevDir = 0;
            opDir = 2;
            break;
            case 1: ghost.setLayoutX(ghost.getLayoutX() - 10);
            prevDir = 1;
            opDir = 3;
            break;
            case 2: ghost.setLayoutY(ghost.getLayoutY() + 10);
            prevDir = 2;
            opDir = 0;
            break;
            case 3: ghost.setLayoutX(ghost.getLayoutX() + 10);
            prevDir = 3;
            opDir = 1;
            break;
        }
    }

    private void blockMove(Sprite ghost){
        switch (prevDir){
            case 0:
                ghost.setLayoutY(ghost.getLayoutY() - 10);
                break;
            case 1: ghost.setLayoutX(ghost.getLayoutX() - 10);
                break;
            case 2: ghost.setLayoutY(ghost.getLayoutY() + 10);
                break;
            case 3: ghost.setLayoutX(ghost.getLayoutX() + 10);
                break;
        }
    }

}
