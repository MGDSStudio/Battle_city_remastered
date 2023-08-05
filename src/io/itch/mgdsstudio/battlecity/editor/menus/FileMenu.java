package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.GuiElement;

import io.itch.mgdsstudio.battlecity.editor.EditorAction;
import io.itch.mgdsstudio.battlecity.editor.EditorCommandPrefix;
import io.itch.mgdsstudio.battlecity.editor.EditorListenersManagerSingleton;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.dataloading.ExternalDataController;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.PlayerTank;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.battlecity.mainpackage.IEngine;
import io.itch.mgdsstudio.engine.libs.data.FileReader;
import io.itch.mgdsstudio.engine.libs.data.FileWriter;

import java.util.ArrayList;

public class FileMenu extends AbstractEditorMenu {
    private String save, clear, exit;
    private String yesClear;

   private interface Statements{
         int REALLY_WANT_TO_SAVE = 11;
         int SAVED = 12;
         int REALLY_WANT_TO_CLEAR = 21;
         int CLEARING = 22;
       
   }

    public FileMenu(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
    }

    @Override
    protected void initGui(){
        if (actualStatement == START_STATEMENT) {
            initButtonNames();
            String [] names = new String[4];
            for (int i = 0; i < names.length; i++){
                names[i] = getNameForPos(i);
            }
            createSubmenuWithDefaultAlignedButtons(names);
        }
        else if (actualStatement == Statements.SAVED){
            String [] names = new String[1];
            names[0] = back;
            createSubmenuWithDefaultAlignedButtons(names);
            saveData();
            editorController.setConsoleText("SUCCESSFULLY SAVED");
        }
        else if (actualStatement == Statements.REALLY_WANT_TO_CLEAR){
            String [] names = new String[]{yesClear, back, cancel};
            createSubmenuWithDefaultAlignedButtons(names);
editorController.setConsoleText(" DO YOU REALLY WANT TO CLEAR THE LEVEL?");

        }
        else if (actualStatement == Statements.CLEARING){
             String [] names = new String []{back};
             createSubmenuWithDefaultAlignedButtons(names);
             editorController.setConsoleText("LEVEL WAS SUCCESSFULLY CLEARED");
             clearLevelData();
        }
    }


    private void clearLevelData(){
       clearActualWorld();
        clearFileData();
        clearUnsavedData();
    }   

    private void clearActualWorld(){
        ArrayList<Entity> gameObjects = editorController.getGameRound().getEntities();
        for (int i = (gameObjects.size()-1); i >= 0; i--) {
            Entity e = gameObjects.get(i);
            if (e instanceof PlayerTank) {

            } else gameObjects.remove(i);
            if (gameObjects.size() == 0)
                editorController.setConsoleText("YOUR LEVEL DOESN'T CONTAIN PLAYER TANKS. YOUR SHOULD ADD AT LEAST ONE TO HAVE THE ABILITY TO PLAY THIS LEVEL");
            else if (gameObjects.size() == 1) {


            } else {
                while (gameObjects.size() > 1) {
                    gameObjects.remove(1);
                }
            }
        }
    }

    private void clearFileData(){
        String fileName = ExternalDataController.getLevelFileName(editorController.getLevelNumber());
        String path = editorController.getEngine().getPathToObjectInUserFolder(fileName);
        FileReader fileReader  = new FileReader();
        ArrayList <String> content = fileReader.getFileContent(path);
        int playersFounded = 0;
        for (String string : content){
            if (string.contains(PlayerTank.class.getSimpleName())){
                playersFounded++;
            }
        }
        if (playersFounded == 0) Logger.debug("Warning - no data about player in the data file");
        else if (playersFounded >= 2) {
            Logger.debug("This level had " + playersFounded + " player's tanks");
            int pos = content.size()-1;
            while (playersFounded>=2){
                if (content.get(pos).contains(PlayerTank.class.getSimpleName())){
                    content.remove(pos);
                    playersFounded--;
                    pos--;
                }
                pos--;
            }
        }
        FileWriter fileWriter = new FileWriter();
        fileWriter.writeDataInFile(path, content);
    }

    private void clearUnsavedData(){
        editorController.getUnsavedDataList().clear();
        EditorAction editorAction = new EditorAction(EditorCommandPrefix.LEVEL_CLEARED);
        EditorListenersManagerSingleton manager = EditorListenersManagerSingleton.getInstance();

        manager.notify(editorAction);
    }

    private void saveData() {
        boolean success =  editorController.getUnsavedDataList().save();

        if (success) {
            editorController.setConsoleText("DATA WAS SUCCESSFULLY SAVED!");
        }
        else editorController.setConsoleText("NOT ALL THE DATA WAS SUCCESSFULLY SAVED");
    }


    private void initButtonNames(){
        save = "SAVE";
        clear = "CLEAR";
        exit = "EXIT";
        yesClear  = "CLEAR MAP";
    }

    private String getNameForPos(int i) {
        String name;
        switch(i) {
            case (0): name =  save; break;
            case (1): name =  clear; break;
            case (2): name =  back; break;
            case (3): name =  exit; break;
            default:  name = "No name"; break;
        }
        return name;
    }

    protected String getTextForConsoleByPressedGui(GuiElement element){
        int ENGLISH = 0;
        int language = ENGLISH;
        if (element.getName() == save){
            return "SAVE CHANGES IN THE ACTUAL LEVEL";
        }
        else if (element.getName() == clear){
            return "CLEAR ALL THE LEVEL DATA";
        }
        else if (element.getName() == back){
            return "BACK IN PREVIOUS MENU";
        }
        else if (element.getName() == exit){
            return "LEAVE THE EDITOR";
        }
        else return "NO DATA";
    }

    @Override
    protected void guiPressed(GuiElement element) {

    }

    @Override
    protected void setConsoleTextForFirstButtonPressing(GuiElement element) {
        editorController.setConsoleText(getTextForConsoleByPressedGui(element));
    }

    @Override
    protected void guiReleased(GuiElement element) {
        if (element.getName().equals(back)) {
            onBackPressed();
        }
        else if (element.getName().equals(save)) {
            nextStatement = Statements.SAVED;
        }
        else if (element.getName().equals(yesClear)){
            nextStatement = Statements.CLEARING;
        }
        else if (element.getName().equals(clear)){
            nextStatement = Statements.REALLY_WANT_TO_CLEAR;
        }
    }

    @Override
    protected void initDataForStatement(int actualStatement) {
        initGui();
    }


    @Override
    protected void onBackPressed(){
       if (actualStatement == START_STATEMENT || actualStatement == Statements.CLEARING || actualStatement == Statements.SAVED) editorController.transferToMenu(MenuType.FILE, MenuType.MAIN);
       else if (actualStatement == Statements.REALLY_WANT_TO_SAVE || actualStatement == Statements.REALLY_WANT_TO_CLEAR){
           nextStatement = START_STATEMENT;
       }
    }
    
}
