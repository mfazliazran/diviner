package com.hacktics.diviner.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;



import com.hacktics.diviner.analyze.SCENARIO_MODE;
import com.hacktics.diviner.gui.controllers.BlinkingButton;

/**
 * 
 * @author Eran Tamari, Shay Chen
 * @since 1.0
 *
 */

public class Parameter extends BlinkingButton implements FocusListener{

	private static final long serialVersionUID = -5196759403817855719L;
	private long parameterID;
	private String pageName;
	private ParameterInfo info;
	private static final int PARAMETER_WIDTH = 90;
	private static final int PARAMETER_HEIGHT = 20;
	private String paramName; 
	private String paramValue;
	private int requestId;
	private JTree advisorTree;
	public static final int PLUGIN_TREE_INDEX = 1;
	public static final int SCENARIO_TREE_INDEX = 2;
	public static final int TARGET_PAGE_INDEX = 3;
	
	
	public static final String PLUGIN_REFLECTION = "Reflection Effects";
	public static final String PLUGIN_DIFF = "Content Diff Effects";
	public static final String PLUGIN_EXCEPTION = "Exception Effects";
	
	public static final Color BLINK_COLOR = Color.RED;
	public static final Color NO_EFFECT_COLOR = Color.WHITE;
	public static final Color EFFECT_COLOR = Color.YELLOW;
	public static final Color SELECTED_NOT_AFFECTING_COLOR = Color.CYAN;


	public static final Boolean AFFECTTING = true;

	public Parameter(long id, int requestId, String pageName, String paramName, String paramValue, boolean affecting, ParameterInfo info) {

		super(NO_EFFECT_COLOR , BLINK_COLOR , paramName);

		this.requestId = requestId;
		this.paramName = paramName;
		this.paramValue = paramValue;
		this.info = info;
		if (affecting) {
			setDefualtColor(EFFECT_COLOR);

			//Add the doubleClick event which opens the payload manager
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						PayloadManager payloadManager = new PayloadManager(Parameter.this.getPageName(), Parameter.this.parameterID);
					}
				}
			});

		}
		else {
			addFocusListener(this);
		}

		RestoreDefaultColor();
		setToolTipText(paramName);
		this.pageName = pageName;
		setFont(new Font("David",Font.BOLD,12));
		setMaximumSize(new Dimension(PARAMETER_WIDTH, PARAMETER_HEIGHT));
		setPreferredSize(new Dimension(PARAMETER_WIDTH, PARAMETER_HEIGHT));
		parameterID = id;
		createAdvisorTreeForDisplay();
		
	}

	private void createAdvisorTreeForDisplay() {
		
		if (info == null) {
			advisorTree = null;
			return;
		}
		TreeData reflectionResults = new TreeData(RESULT_TYPE.REFLECTION);
		TreeData diffResults = new TreeData(RESULT_TYPE.DIFF);
		TreeData exceptionResults = new TreeData(RESULT_TYPE.EXCEPTION);

		TreeData[] resultTypeParents = new TreeData[3];
		resultTypeParents[RESULT_TYPE.REFLECTION.ordinal()] = reflectionResults;
		resultTypeParents[RESULT_TYPE.DIFF.ordinal()] = diffResults;
		resultTypeParents[RESULT_TYPE.EXCEPTION.ordinal()] = exceptionResults;


		DefaultMutableTreeNode parent = new DefaultMutableTreeNode("Analysis Results");
		advisorTree = new JTree(parent);

		//Sort the different behavior found in order to present them
		for (ParameterEffect effect : info.getEffectsList()) {
			SCENARIO_MODE scenarioIndex = effect.getScenario();
			
			if (effect.isDiff()) {
				resultTypeParents[RESULT_TYPE.DIFF.ordinal()].addScenario(scenarioIndex, effect);
			}

			if (effect.isReflection()) {
				resultTypeParents[RESULT_TYPE.REFLECTION.ordinal()].addScenario(scenarioIndex, effect);
			}

			if (effect.isException()) {
				resultTypeParents[RESULT_TYPE.EXCEPTION.ordinal()].addScenario(scenarioIndex, effect);
			}
		}

		//Create result type tree nodes and their children
		for (TreeData resultTypeNodes : resultTypeParents) {

			DefaultMutableTreeNode resultTypeParent = new DefaultMutableTreeNode(resultTypeNodes.getResultType().getText());
			if (resultTypeNodes.getTotalChildrenCount() > 0) {
				parent.add(resultTypeParent);
			}

			DefaultMutableTreeNode loginFirstsNode = new DefaultMutableTreeNode(SCENARIO_MODE.LOGIN_FIRST.text);
			DefaultMutableTreeNode loginAfterNode = new DefaultMutableTreeNode(SCENARIO_MODE.LOGIN_AFTER.text);
			DefaultMutableTreeNode publicNode = new DefaultMutableTreeNode(SCENARIO_MODE.DIRECT.text);



			//Add login first Node
			if (resultTypeNodes.loginFirstResults.size() > 0) {
				resultTypeParent.add(loginFirstsNode);
			}

			//Add target page nodes
			for (String reflectionTarget : resultTypeNodes.loginFirstResults.keySet()) {
				loginFirstsNode.add(new DefaultMutableTreeNode(reflectionTarget));

			}


			//Add public Node
			if (resultTypeNodes.publicResults.size() > 0) {
				resultTypeParent.add(publicNode);
			}

			//Add target page nodes
			for (String diffTarget : resultTypeNodes.publicResults.keySet()) {
				publicNode.add(new DefaultMutableTreeNode(diffTarget));
			}


			//Add login after Node
			if (resultTypeNodes.loginAfterResults.size() > 0) {
				resultTypeParent.add(loginAfterNode);
			}

			//Add target page nodes
			for (String exceptionsTarget : resultTypeNodes.loginAfterResults.keySet()) {
				loginAfterNode.add(new DefaultMutableTreeNode(exceptionsTarget));

			}
		}
	}
	
	class TreeData {

		private HashMap<String, ParameterEffect> loginFirstResults;
		private HashMap<String, ParameterEffect> loginAfterResults;
		private HashMap<String, ParameterEffect> publicResults;
		private int childrenCount;
		private RESULT_TYPE resultType;

		public TreeData(RESULT_TYPE resultType) {
			loginFirstResults = new  HashMap<String, ParameterEffect>();
			loginAfterResults = new  HashMap<String, ParameterEffect>();
			publicResults = new  HashMap<String, ParameterEffect>();
			childrenCount = 0;
			this.resultType = resultType;
		}

		public void addScenario(SCENARIO_MODE scenarioIndex, ParameterEffect effect) {
			int i = SCENARIO_MODE.LOGIN_FIRST.ordinal();
			switch (scenarioIndex) {

			case DIRECT:
				addPublicResult(effect);
				break;
			case LOGIN_AFTER:
				addLoginAfterResults(effect);
				break;
			case LOGIN_FIRST:
				addLoginFirstResult(effect);
				break;
				
			}
		}
		
		public RESULT_TYPE getResultType() {
			return resultType;
		}

		public void addLoginFirstResult(ParameterEffect effect) {
			loginFirstResults.put(effect.getOutputPage(), effect);
			childrenCount ++;
		}

		public void addLoginAfterResults(ParameterEffect effect) {
			loginAfterResults.put(effect.getOutputPage(), effect);
			childrenCount ++;

		}

		public void addPublicResult(ParameterEffect effect) {
			publicResults.put(effect.getOutputPage(), effect);
			childrenCount ++;
		}

		public int getTotalChildrenCount() {
			return childrenCount;
		}
	}
	
	public JTree getAdvisorTree() {
		return advisorTree;
	}
	
	public long getParameterID()
	{
		return parameterID;
	}

	public String getPageName()
	{
		return pageName;
	}


	public ArrayList<ParameterEffect> getAffectedPages()
	{
		if (info != null) {
			return info.getEffectsList();
		}
		return null;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public boolean isAffecting() {
		
		return (info != null && info.getEffectsList() != null);
	}

	public ParameterInfo getInfo() {
		if (info != null) {
			return info;
		}
		return null;
	}

	@Override
	public void focusGained(FocusEvent e) {
		this.setBackground(SELECTED_NOT_AFFECTING_COLOR);
	}

	@Override
	public void focusLost(FocusEvent e) {
		this.setBackground(NO_EFFECT_COLOR);
	}


}
