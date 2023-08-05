package io.itch.mgdsstudio.battlecity.editor.menus;

import com.mgdsstudio.engine.nesgui.GuiElement;

import io.itch.mgdsstudio.battlecity.editor.*;
import io.itch.mgdsstudio.battlecity.editor.data.UnsavedDataList;
import io.itch.mgdsstudio.battlecity.game.EditorController;
import io.itch.mgdsstudio.battlecity.game.Logger;
import io.itch.mgdsstudio.battlecity.game.gameobjects.Entity;
import io.itch.mgdsstudio.battlecity.game.gameobjects.controllers.ISelectable;
import io.itch.mgdsstudio.battlecity.game.hud.LowerPanelInEditor;
import io.itch.mgdsstudio.engine.libs.Geometrie;
import processing.core.PApplet;

import java.util.ArrayList;

public class EditMenu extends AbstractEditorMenu implements EditorActionsListener {

    private ArrayList<EditorAction> actions = new ArrayList<>();
    private String select, copy, move, clearSelection, remove;  //main
    private String delete;
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
       else if (actualStatement == Statements.REMOVE){           ;
           delete = "DELETE " + editorController.getSelectedObjects().size();
           String [] names = new String[]{delete, back, cancel};
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
        delete = "DELETE";
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
        editorController.setConsoleText(getTextForConsoleByPressedGui(element));
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
      else if (element.getName().equals(addToSelected)){
          updateAddToSelection();
        }
      else if (element.getName().equals(delete)){
          updateDeleting();
        }
    }

    private void updateDeleting() {
       int count = editorController.getSelectedObjects().size();
       int deleted = 0;
       nextStatement = START_STATEMENT;
       for (int i = (editorController.getSelectedObjects().size()-1); i>= 0; i--){
           try {
               removeObjectFromDataFile(editorController.getSelectedObjects().get(i));
               removeObjectFromUnsavedData(editorController.getSelectedObjects().get(i));
               editorController.getGameRound().removeEntity((Entity) editorController.getSelectedObjects().get(i));

               deleted++;
           }
           catch (Exception e){
               Logger.error("Can not remove from map " + editorController.getSelectedObjects().get(i).getDataString() + e);
           }
       }
       editorController.setConsoleText(deleted  + " WERE DELETED FROM MAP FROM " + count);
    }

    private void removeObjectFromDataFile(ISelectable iSelectable) {

    }

    private void removeObjectFromUnsavedData(ISelectable iSelectable) {
       UnsavedDataList list  = editorController.getUnsavedDataList();
       ArrayList <String> data = list.getData();
       String toBeDeleted = iSelectable.getDataString();
       for (int i = (data.size()-1); i>= 0; i--){
           String savedString = data.get(i);
           if (savedString.equals(toBeDeleted)){
               data.remove(i);
               Logger.debug("Object with string: " + toBeDeleted + " was deleted also from unsaved data list");
           }
           else Logger.debug("String " + toBeDeleted + " is not same as " + savedString);
       }
    }

    private void updateAddToSelection() {
       ArrayList <ISelectable> selected = getObjectsUnderCursor(editorController.getCursor());
       int count = selected.size();
       if (count > 0){
           editorController.setConsoleText(count + " OBJECTS WERE SELECTED");
           for (ISelectable s : selected){
               s.setSelected(true);
           }
           editorController.getSelectedObjects().addAll(selected);
       }
       else editorController.setConsoleText("NOTHING WAS SELECTED");
    }

    protected void clearSelection(){
        ArrayList <ISelectable> selected  = editorController.getSelectedObjects();
        editorController.setConsoleText(selected.size() + " OBJECTS WERE REMOVED FROM SELECTION");
        for (int i = (selected.size()-1); i >= 0;i --){
            ISelectable part = selected.get(i);
            if (part == null) Logger.error("This can not be. ");
            else part.setSelected(false);
            selected.remove(i);
        }
        nextStatement = START_STATEMENT;
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

    private ArrayList<ISelectable> getObjectsUnderCursor(Cursor cursor) {
        ArrayList <ISelectable> selectables = new ArrayList<>();
        float cursorCenterX = cursor.getActualCrossPos().x;
        float cursorCenterY = cursor.getActualCrossPos().y;
        float cursorW = cursor.getWidth();
        float cursorH = cursor.getHeight();
        for (Entity entity : editorController.getGameRound().getEntities()){
            if (entity instanceof ISelectable){
                if (!selectables.contains(entity)){
                    float entityCenterX = entity.getPos().x;
                    float entityCenterY = entity.getPos().y;
                    float entityAabbWidth = (entity.getWidth()* PApplet.cos(PApplet.radians(entity.getAngle())));
                    float entityAabbHeight = (entity.getHeight()* PApplet.sin(PApplet.radians(entity.getAngle())));
                    boolean inside = Geometrie.isIntersectionBetweenAllignedRects(entityCenterX, entityCenterY, entityAabbWidth, entityAabbHeight, cursorCenterX, cursorCenterY, cursorW, cursorH);
                    if (inside){
                        selectables.add((ISelectable) entity);
                    }
                }
            }
        }
        return selectables;
    }
}
