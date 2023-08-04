package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.GuiElement;

import io.itch.mgdsstudio.battlecity.editor.*;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.libs.Geometrie;

import java.util.ArrayList;

public class EditMenu extends AbstractEditorMenu implements EditorActionsListener {

    private ArrayList<EditorAction> actions = new ArrayList<>();
    private String select, copy, move, clearSelection, remove;  //main
    private String addToSelected;


   private interface Statements{
         int SELECT = 11;
         int SELECTED_MORE_THAN_ONE = 12;
         int COPY = 21;
         int MOVE = 31;
         //int CLEARING = 22;  NO CLEARING STATEMENT
         int REMOVE = 41;
    // int BACK = NO_BACK_ACTION
   }

    public EditMenu(EditorController editorController, LowerPanelInEditor lowerPanelInEditor) {
        super(editorController, lowerPanelInEditor, NO_END);
        EditorListenersManagerSingleton.getInstance().addAsListener(this);
    }

    @Override
    protected void initGui(){
       if (actualStatement == START_STATEMENT) {
           editorController.getCursor().setStatement(Cursor.Statement.INVISIBLE_AS_CELL_CENTER);
           initButtonNames();
           String [] names = new String[]{select, copy, move, clearSelection, remove, back};
           createSubmenuWithDefaultAlignedButtons(names);
           hideButtonsInStartSubmenu();
       }
        else if (actualStatement == Statements.SELECT){
           String [] names = new String[]{addToSelected, back, cancel};
           createSubmenuWithDefaultAlignedButtons(names);
       }
    }

    private void hideButtonsInStartSubmenu() {
       if (editorController.getSelectedObjects().size()==0){
           getGuiByName(remove).setActualStatement(GuiElement.BLOCKED);
           getGuiByName(copy).setActualStatement(GuiElement.BLOCKED);
           getGuiByName(move).setActualStatement(GuiElement.BLOCKED);
           getGuiByName(clearSelection).setActualStatement(GuiElement.BLOCKED);
       }
    }

    private void initButtonNames(){
        select = "SELECT";
        copy = "COPY";
        move = "MOVE";
        clearSelection = "CLEAR SELECTION";
        remove = "REMOVE";
        addToSelected = "SELECT OBJECT";
    }

    protected String getTextForConsoleByPressedGui(GuiElement element){
        int ENGLISH = 0;
        int language = ENGLISH;
        if (element.getName() == select){
            return "SELECT ONE OR MORE OBJECTS TO COPY OR DELETE THEM";
        }
        else if (element.getName() == copy){
            return "COPY SELECTED OBJECTS";
        }
        else if (element.getName() == move){
            return "MOVE SELECTED OBJECTS";
        }
        else if (element.getName() == clearSelection){
            return "CLEAR SELECTION";
        }
        else if (element.getName() == remove){
             return "REMOVE SELECTED OBJECTS FROM GAME WORLD";
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
       else if (element.getName().equals(select)){
         nextStatement =   Statements.SELECT;

      }
    else if (element.getName().equals(copy)){
         nextStatement =   Statements.COPY;
          }
    else if (element.getName().equals(move)){
         nextStatement =   Statements.MOVE;
    }
    else if (element.getName().equals(remove)){
          nextStatement = Statements.REMOVE;
      }
      else if (element.getName().equals(clearSelection)){
            clearSelection();
      }
    }

    protected void clearSelection(){

  }

    @Override
    protected void initDataForStatement(int actualStatement) {
        initGui();
    }


    @Override
    protected void onBackPressed(){
          editorController.transferToMenu(MenuType.FILE, MenuType.MAIN);
    }

    @Override
    public void appendCommand(EditorAction action) {
        actions.add(action);
    }

    @Override
    public void update(){
       super.update();
        if (actions.size()>0){
            for (int i = (actions.size()-1); i >= 0; i--){
                if (actions.get(i).getPrefix().equals(EditorCommandPrefix.WORLD_SCROLLING_ENDED)){
                     if (getObjectsUnderCursor(editorController.getCursor()).size()<=0){
                         editorController.getCursor().setStatement(Cursor.Statement.INVISIBLE_AS_CELL_CENTER);
                     }
                     else editorController.getCursor().setStatement(Cursor.Statement.CELL_CENTER);
                }
                else if (actions.get(i).getPrefix().equals(EditorCommandPrefix.WORLD_SCROLLING_STARTED)){
                    editorController.getCursor().setStatement(Cursor.Statement.CELL_CENTER);
                }
                actions.remove(i);
            }
        }
        if (actions.size()>20){
            Logger.error("Too many actions " + actions.size());
        }
       }

    private ArrayList<Entity> getObjectsUnderCursor(Cursor cursor) {
        float cursorCenterX = cursor.getActualCrossPos().x;
        float cursorCenterY = cursor.getActualCrossPos().y;
        float cursorW = cursor.getWidth();
        float cursorH = cursor.getHeight();
        for (Entity entity : editorController.getGameRound().getEntities()){
            if ()
        }
    }
}
