/* Java Flash Cards
 * Copyright (C) 2010, Ronald Kinard <furyhunter600@gmail.com>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *    
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  
 *  * Neither the name of Ronald Kinard nor the names of his contributors may be
 *    used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */

package info.furyhunter.flashcards;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class FlashCards {
	public static final int FRONT = 0;
	public static final int BACK = 1;
	private int side = FRONT;
	private int cardIndex = 0;
	private ArrayList<Card> cards;

	private Label cardLabel;

	public FlashCards(final Shell shell) {
		shell.setText("Java Flash Cards");

		// Set up data
		cards = new ArrayList<Card>();
		cards.add(0, new Card("Default Cards", "Open a card set!"));

		// Menu
		Menu menuBar = new Menu(shell, SWT.BAR);
		Menu fileMenu = new Menu(menuBar);
		MenuItem fileItem = new MenuItem(menuBar, SWT.CASCADE);
		fileItem.setText("File");
		fileItem.setMenu(fileMenu);
		MenuItem openItem = new MenuItem(fileMenu, SWT.NONE);
		openItem.setText("Open...");
		openItem.setAccelerator(SWT.CONTROL | 'o');
		openItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell);
				dialog.setFilterExtensions(new String[] { "*.cards.txt" });
				dialog.setFilterNames(new String[] { "Card Set (*.cards.txt)" });
				dialog.setText("Open Card Set...");
				String file = dialog.open();
				if (file != null) {
					File fileFile = new File(file);
					readCards(fileFile);
					shell.setText("Java Flash Cards - " + fileFile.getName());
					randomCard();
					side = FRONT;
					updateLabel();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});
		MenuItem exitItem = new MenuItem(fileMenu, SWT.NONE);
		exitItem.setText("Exit");
		exitItem.setAccelerator(SWT.ALT | SWT.F4);
		exitItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		shell.setMenuBar(menuBar);

		GridLayout grid = new GridLayout();
		grid.numColumns = 4;
		grid.makeColumnsEqualWidth = true;
		shell.setLayout(grid);
		GridData data = new GridData();
		data.horizontalSpan = 4;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		cardLabel = new Label(shell, SWT.WRAP);
		cardLabel.setLayoutData(data);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;

		Button button = new Button(shell, SWT.PUSH);
		button.setLayoutData(data);
		button.setText("Flip");
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				flipCard();
				updateLabel();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		button = new Button(shell, SWT.PUSH);
		button.setLayoutData(data);
		button.setText("Random");
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				randomCard();
				side = FRONT;
				updateLabel();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		button = new Button(shell, SWT.PUSH);
		button.setLayoutData(data);
		button.setText("< Previous");
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				previousCard();
				side = FRONT;
				updateLabel();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		button = new Button(shell, SWT.PUSH);
		button.setLayoutData(data);
		button.setText("Next >");
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				nextCard();
				side = FRONT;
				updateLabel();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		// Init
		shell.setMinimumSize(300, 200);
		shell.setSize(500, 300);
		shell.pack();
		shell.open();
		updateLabel();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		new FlashCards(shell);
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}
	}

	public void flipCard() {
		if (side == FRONT) {
			side = BACK;
		} else {
			side = FRONT;
		}
	}

	public void nextCard() {
		if (cardIndex == cards.size() - 1) {
			cardIndex = 0;
		} else {
			cardIndex += 1;
		}
	}

	public void previousCard() {
		if (cardIndex == 0) {
			cardIndex = cards.size() - 1;
		} else {
			cardIndex -= 1;
		}
	}

	public void randomCard() {
		cardIndex = (int) (Math.round(cards.size() * Math.random()));
		if (cardIndex == cards.size()) {
			cardIndex -= 1;
		}
	}

	public void updateLabel() {
		if (side == FRONT) {
			cardLabel.setText(cards.get(cardIndex).frontSide);
		}
		if (side == BACK) {
			cardLabel.setText(cards.get(cardIndex).backSide);
		}
	}

	public void readCards(File file) {
		cards.clear();
		try {
			Scanner scanner = new Scanner(file);
			String front;
			String back;
			while (scanner.hasNextLine()) {
				front = scanner.nextLine();
				back = scanner.nextLine();
				cards.add(new Card(front, back));
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return;
		}
	}
}
