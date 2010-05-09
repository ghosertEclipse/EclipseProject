package com.loadtrend.app.trees;

import java.util.Collection;
import java.util.Observable;

import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IEditorInput;

public class TreeObject extends Observable implements IActionFilter
{
	private String name = null;

	private TreeParent parent = null;
	
	private IEditorInput editorInput = null;
	
	private Collection collection = null; 

	public TreeObject( String name )
	{
		this.name = name;
	}
    
    public void setName( String name )
    {
        this.name = name;
    }
    
	public String getName()
	{
		return name;
	}

	public void setParent( TreeParent parent )
	{
		this.parent = parent;
	}

	public TreeParent getParent()
	{
		return parent;
	}
	
	public void setEditorInput( IEditorInput editorInput )
	{
		this.editorInput = editorInput;
	}
	
	public IEditorInput getEditorInput()
	{
		return this.editorInput;
	}
	
	/**
	 * @return Returns the collection.
	 */
	public Collection getCollection()
	{
		return collection;
	}

	/**
	 * @param collection The collection to set.
	 */
	public void setCollection( Collection collection )
	{
		this.collection = collection;
        
        // Set the changes and notify the Observer
        this.setChanged();
        this.notifyObservers( collection );
	}

	public String toString()
	{
		return getName();
	}

	public String getTreeName()
	{
		String treeName = this.getName();
		TreeParent parent = this.getParent();
		while ( parent != null && !parent.getName().equals( "" ) )
		{
			treeName = treeName + " - " + parent.getName();
			parent = parent.getParent();
		}
		return treeName;
	}

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
     */
    public boolean testAttribute(Object target, String name, String value) {
        if (this.collection != null && this.collection.size() > 0) return true;
        return false;
    }
}
