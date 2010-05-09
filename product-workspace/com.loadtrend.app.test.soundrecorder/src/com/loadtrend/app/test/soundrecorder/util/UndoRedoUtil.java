package com.loadtrend.app.test.soundrecorder.util;

import java.util.LinkedList;

public class UndoRedoUtil {
	
	private LinkedList actionList = new LinkedList();
	
	private int currentAction = -1;
	
	private ActionListChangedListener listener = null;
	
    public interface ActionListChangedListener {
    	public void notifyToUndo();
    	public void noAvailableUndo();
    	public void noAvailableRedo();
    	public void notifyToRedo();
    }
	
	public abstract class Action {
		public abstract void undo();
		public abstract void redo();
	}
	
	public void addAction(Action action) {
		int toBeAbortAction = actionList.size() - 1 - currentAction;
		if (toBeAbortAction > 0) {
			for (int i = 0; i < toBeAbortAction; i++) {
				actionList.removeLast();
			}
			if (listener != null) listener.noAvailableRedo();
		}
		actionList.add(action);
		currentAction = actionList.size() - 1;
		if (this.listener != null) this.listener.notifyToUndo();
	}
	
	public void undo() {
		if (actionList.size() == 0) return;
		if (currentAction == -1) return;
		((Action) actionList.get(currentAction)).undo();
		currentAction = currentAction - 1;
		if ((currentAction == -1) && listener != null) listener.noAvailableUndo();
		if (listener != null) listener.notifyToRedo();
	}
	
	public void redo() {
		if (actionList.size() == 0) return;
		if (currentAction == actionList.size() - 1) return;
		currentAction = currentAction + 1;
		((Action) actionList.get(currentAction)).redo();
		if ((currentAction == actionList.size() - 1) && listener != null) listener.noAvailableRedo();
		if (listener != null) listener.notifyToUndo();
	}
	
	public void addActionListChangedListener(ActionListChangedListener listener) {
		this.listener = listener;
	}
	
	public void clear() {
		this.currentAction = -1;
		this.actionList.clear();
	}
}
