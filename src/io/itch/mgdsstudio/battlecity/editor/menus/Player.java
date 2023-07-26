package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.ButtonWithFrameSelection;
import com.mgdsstudio.engine.nesgui.GuiElement;
import io.itch.mgdsstudio.battlecity.editor.Cross;
import io.itch.mgdsstudio.battlecity.editor.ISelectable;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;

import java.awt.*;
import java.util.ArrayList;

public class Player extends AbstractEditorMenu {

    private String placePlayer, removePlayers;  //MAIN MENU
    private String addPlayer, yesIWant;

    // private String select

    private interface Statements{
        int PLACE_PLAYER = 11;
        int ARE_YOU_SURE_YOU_WANT_TO_DELETE = 111;
        int DELETE_PLAYER = 21;


    }

    public Player(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
        editorController.getCross().setStatement(Cross.Statement.INVISIBLE_AS_CELL_CENTER);
    }

    @Override
    protected void initGui(){
        if (guiElements.size() > 0) guiElements.clear();
        initButtonNames();
        int buttons = 3;
        Rectangle[] zones = getCoordinatesForDefaultButtonsAlignment(buttons);
        for (int i = 0; i < buttons; i++){
            GuiElement gui = new ButtonWithFrameSelection(editorController.getEngine(), zones[i].x, zones[i].y, zones[i].width, zones[i].height, getNameForPos(i), editorController.getEngine().getEngine().g, true);
            guiElements.add(gui);
        }
    }

    private void initButtonNames(){
        //select, copy, move, clearSelection, delete, back;
        placePlayer = "PLACE PLAYER";
        removePlayers = "DELETE PLAYER";
        yesIWant = "YES I WANT";
        addPlayer = "ADD";
    }

    private String getNameForPos(int i) {
          String name;
        switch(i) {
            case (0): name =  placePlayer; break;
            case (1): name = removePlayers; break;
            default:  name = back; break;
        }
        return name;
    }

    protected String getTextForConsoleByPressedGui(GuiElement element){
        int ENGLISH = 0;
        int language = ENGLISH;
        if (element.getName() == placePlayer){
            return "PLACE NEW PLAYER ON MAP";
        }
        else if (element.getName() == removePlayers){
            return "REMOVE ALL PLAYERS FROM MAP";
        }
        else if (element.getName() == addPlayer){
            return "ADD PLAYER";
        }
        else if (element.getName() == back){
            return "BACK IN PREVIOUS MENU";
        }
        else return "NO DATA";
    }



    @Override
    protected void guiPressed(GuiElement element) {

    }

    //transfer in parent
    @Override
    protected void setConsoleTextForFirstButtonPressing(GuiElement element) {
        editorController.setTextInConcole(getTextForConsoleByPressedGui(element));
    }

    @Override
    protected void guiReleased(GuiElement element) {
        if (element.getName().equals(back)) {
            onBackPressed();
        }
        else if (element.getName().equals(placePlayer)){
            nextStatement =   Statements.PLACE_PLAYER;
        }
        else if (element.getName().equals(removePlayers)){
            nextStatement = Statements.DELETE_PLAYER;
        }
        else if (element.getName().equals(addPlayer)){
            addPlayer();
            
        }
        else if (element.getName().equals(yesIWant)){
            removePlayer();
            nextStatement = START_STATEMENT;
        }
    }

    private void addPlayer(){
        Entity <ArrayList> gameObjects = editorController.getGameRound().getEntities();
        
    }

    private void removePlayer(){
        Entity <ArrayList> gameObjects = editorController.getGameRound().getEntities();
        for (int i = (gameObjects.size()-1); i >= 0; i--){
            if (gameObjects.get(i) instanceof PlayerTank){
                gameObjects.remove(i);
            }
        }
    }

    
    @Override
    protected void initDataForStatement(int actualStatement) {
        guiElements.clear();
        String consoleText = "";
        if (actualStatement == Statements.PLACE_PLAYER){
            consoleText = "PLACE THE BUTTON ADD TO ADD A PLAYER. IF YOU HAVE MORE THAN ONE PLAYERS - THEY WILL BE PLAYABLE ONLY IN MULTIPLAYER MODE";
            int buttons = 2;
            Rectangle[] zones = getCoordinatesForDefaultButtonsAlignment(buttons);
            GuiElement add = new ButtonWithFrameSelection(editorController.getEngine(), zones[0].x, zones[0].y, zones[0].width, zones[0].height, addPlayer, editorController.getEngine().getEngine().g, true);
            guiElements.add(add);
            GuiElement backButton = new ButtonWithFrameSelection(editorController.getEngine(), zones[1].x, zones[1].y, zones[1].width, zones[1].height, back, editorController.getEngine().getEngine().g, true);
            guiElements.add(backButton);
            editorController.getCross().setStatement(Cross.Statement.CELL_CENTER);
        }
        else if (actualStatement == Statements.ARE_YOU_SURE_YOU_WANT_TO_DELETE){
            consoleText = "DO YOU REALLY WANT TO DELETE ALL THE PLAYERS FROM THE MAP?";
            int buttons = 2;
            Rectangle[] zones = getCoordinatesForDefaultButtonsAlignment(buttons);
            GuiElement add = new ButtonWithFrameSelection(editorController.getEngine(), zones[0].x, zones[0].y, zones[0].width, zones[0].height, yesIWant, editorController.getEngine().getEngine().g, true);
            guiElements.add(add);
            GuiElement backButton = new ButtonWithFrameSelection(editorController.getEngine(), zones[1].x, zones[1].y, zones[1].width, zones[1].height, back, editorController.getEngine().getEngine().g, true);
            guiElements.add(backButton);
            editorController.getCross().setStatement(Cross.Statement.CELL_CENTER);
        }
        else if (actualStatement == START_STATEMENT){
            initGui();
        }
        editorController.setTextInConcole(consoleText);

    }


    @Override
    protected void onBackPressed(){
        //Logger.debug("Back pressed by statement: " + actualStatement);
        if (actualStatement == START_STATEMENT) {

            editorController.transferToMenu(MenuType.PLAYER, MenuType.MAIN);
        }
        else if (actualStatement == Statements.PLACE_PLAYER) nextStatement = START_STATEMENT;
        else if (actualStatement == Statements.DELETE_PLAYER) nextStatement = START_STATEMENT;
        else {
            nextStatement = START_STATEMENT;
        }

    }

}
