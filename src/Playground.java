import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Playground {
    private ArrayList<Sprite> environment = new ArrayList<>();

    public Playground(String pathName) {
        try {
            final Image imageTree = ImageIO.read(new File("./img/tree.png"));
            final Image imageGrass = ImageIO.read(new File("./img/grass.png"));
            final Image imageRock = ImageIO.read(new File("./img/rock.png"));
            final Image imageTrap = ImageIO.read(new File("./img/trap.png"));

            final int imageTreeWidth = imageTree.getWidth(null);
            final int imageTreeHeight = imageTree.getHeight(null);

            final int imageGrassWidth = imageGrass.getWidth(null);
            final int imageGrassHeight = imageGrass.getHeight(null);

            final int imageRockWidth = imageRock.getWidth(null);
            final int imageRockHeight = imageRock.getHeight(null);

            final int imageTrapWidth = imageTrap.getWidth(null);
            final int imageTrapHeight = imageTrap.getHeight(null);

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathName))) {
                String line;
                int lineNumber = 0;
                int columnNumber = 0;

                while ((line = bufferedReader.readLine()) != null) {
                    for (byte element : line.getBytes(StandardCharsets.UTF_8)) {
                        // Ajouter des sprites en fonction du caractère
                        switch (element) {
                            case 'T':
                                environment.add(new SolidSprite(columnNumber * imageTreeWidth,
                                        lineNumber * imageTreeHeight, imageTree, imageTreeWidth, imageTreeHeight));
                                break;
                            case ' ':
                                environment.add(new Sprite(columnNumber * imageGrassWidth,
                                        lineNumber * imageGrassHeight, imageGrass, imageGrassWidth, imageGrassHeight));
                                break;
                            case 'R':
                                environment.add(new SolidSprite(columnNumber * imageRockWidth,
                                        lineNumber * imageRockHeight, imageRock, imageRockWidth, imageRockHeight));
                                break;

                            case 'G':
                                environment.add(new SolidSprite(columnNumber * imageTrapWidth,
                                        lineNumber * imageTrapHeight, imageTrap, imageTrapWidth, imageTrapHeight));
                                break;

                        
                        }
                        columnNumber++;
                    }
                    columnNumber = 0;
                    lineNumber++;
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la lecture du fichier : " + pathName);
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Sprite> getSolidSpriteList() {
        ArrayList<Sprite> solidSpriteArrayList = new ArrayList<>();
        for (Sprite sprite : environment) {
            if (sprite instanceof SolidSprite) solidSpriteArrayList.add(sprite);
        }
        return solidSpriteArrayList;
    }

    public ArrayList<Displayable> getSpriteList() {
        ArrayList<Displayable> displayableArrayList = new ArrayList<>();
        for (Sprite sprite : environment) {
            displayableArrayList.add((Displayable) sprite);
        }
        return displayableArrayList;
    }

}
