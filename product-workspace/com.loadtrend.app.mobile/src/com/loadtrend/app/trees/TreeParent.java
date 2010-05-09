package com.loadtrend.app.trees;

import java.util.ArrayList;
import java.util.Iterator;

public class TreeParent extends TreeObject
{
	private ArrayList children;

	public TreeParent( String name )
	{
		super( name );
		children = new ArrayList();
	}

	public void addChild( TreeObject child )
	{
		children.add( child );
		child.setParent( this );
	}

	public void removeChild( TreeObject child )
	{
		children.remove( child );
		child.setParent( null );
	}

	public TreeObject[] getChildren()
	{
		return (TreeObject[]) children.toArray( new TreeObject[children.size()] );
	}
	
	public TreeObject getChildren( String name )
	{
		Iterator it = children.iterator();
		while ( it.hasNext() )
		{
			TreeObject to = (TreeObject) it.next();
			if ( to.getName().startsWith( name ) )
			{
				return to;
			}
		}
		return null;
	}

	public boolean hasChildren()
	{
		return children.size() > 0;
	}
}
