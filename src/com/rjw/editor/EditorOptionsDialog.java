package com.rjw.editor;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.rjw.editor.MapEditor.MapEditorActionListener;
import com.rjw.gameskeleton.Level;

/**
 * Our editor options dialog. For now, houses level config options
 * @author rwalsh
 *
 */
public class EditorOptionsDialog extends JDialog{

	private static final long serialVersionUID = 2727201055047755871L;
	public static final String LOOP_CHECKBOX_STRING = "Loop Level (for Boss levels)";
	public static final String APPLY_BUTTON_STRING = "Apply & Close";
	public static final String CANCEL_BUTTON_STRING = "Cancel";

	// our visual UI components
	private JCheckBox _optionsLoopLevelCheckbox;
	private JTextField _optionslevelNameTextfield;
	private JPanel _optionsPanel;
	private JButton _applyButton;
	private JButton _cancelButton;
	
	/**
	 * constructor - inits all our options dialog stuff
	 */
	public EditorOptionsDialog(MapEditor parentMapEditor){
		
		_optionsLoopLevelCheckbox = new JCheckBox("Loop Level (for Boss Levels)");
		_optionslevelNameTextfield = new JTextField("");
		_optionslevelNameTextfield.setColumns(20);
		_optionsPanel = new JPanel();
		_applyButton = new JButton(APPLY_BUTTON_STRING);
		_cancelButton = new JButton(CANCEL_BUTTON_STRING);

		this.setSize(250, 200);
		this.setModal(true);
		
		// apply options
		//JButton applyButton = new JButton("Apply");
		_applyButton.addActionListener(parentMapEditor.new MapEditorActionListener());
		_applyButton.setActionCommand(MapEditorActionListener.MAP_EDITOR_ACTION_APPLY_OPTIONS);
		
		//JButton cancelButton = new JButton("Cancel");
		_cancelButton.addActionListener(parentMapEditor.new MapEditorActionListener());
		_cancelButton.setActionCommand(MapEditorActionListener.MAP_EDITOR_ACTION_CANCEL_OPTIONS);
		

		this.add(_optionsPanel);
		_optionsPanel.add(_optionslevelNameTextfield);
		_optionsPanel.add(_optionsLoopLevelCheckbox);
		_optionsPanel.add(_applyButton);
		_optionsPanel.add(_cancelButton);
		
	}//constructor

	/**
	 * Refreshes the values of the dialog options fields to be
	 * the same as the level passed
	 * @param level
	 */
	public void refreshLevelOptionValues(Level level){
	
		_optionslevelNameTextfield.setText(level.getLevelName());
		
		if(level.getLoopState() == Level.LEVEL_LOOP_FALSE){
			_optionsLoopLevelCheckbox.setSelected(false);
		}else{
			_optionsLoopLevelCheckbox.setSelected(true);
		}
		
	}//refreshLevelOptionsValues
		
	public boolean getLevelLoopValue() {
		return _optionsLoopLevelCheckbox.isSelected();
	}

	public String getLevelNameValue() {
		return _optionslevelNameTextfield.getText();
	}

	
}//EditorOptionsDialog
