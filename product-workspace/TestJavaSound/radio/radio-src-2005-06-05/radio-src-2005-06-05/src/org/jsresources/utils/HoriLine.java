/* *******************************************************
 *
 * name:          HoriLine.java
 *
 * description:   Displays a horizontal line
 *
 * Author:        Florian Bomers
 *
 * Versions:
 * 02 Dec 99: added the possibility of edges,
 *            made it a Lightweight component
 * 22 Nov 99: moved to package bome.util.awt
 * 18 Oct 99: changed package structure
 *
 * (c) 1997-1999  JaWavedit-Team
 *
 ********************************************************* */

/*
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jsresources.utils;

import java.awt.*;

/**
* This small component just displays a horizontal line.
* It is useful to separate sections in a window.
* For example, this command is used to display the
* line right under the menu in JaWavedit:<BR>
* <pre>
*   P.add("North",new HoriLine());
* </pre>
*/

public class HoriLine extends Component {
	private int topEdge=0;
	private int bottomEdge=0;

	public HoriLine() {
		this(0,0);
	}

	public HoriLine(int edge) {
		this(edge,edge);
	}

	public HoriLine(int topEdge, int bottomEdge) {
		this.topEdge=topEdge;
		this.bottomEdge=bottomEdge;
	}

  public void paint(Graphics g) {
    Dimension dim = getSize();
    // mettre la ligne au milieu
    int middle=((dim.height-topEdge-bottomEdge)/2)-1+topEdge;
    g.setColor(Color.gray);
    g.drawLine(0,middle,dim.width,middle);
    g.setColor(Color.white);
    g.drawLine(0,middle+1,dim.width,middle+1);
  }

  public Dimension getPreferredSize() {
    return new Dimension(0,2+topEdge+bottomEdge);
  }
}