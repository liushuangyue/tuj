package org.talend.top.swtbot.test.analysis.columnanalysis;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.swt.finder.matchers.WidgetOfType;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.junit.Before;
import org.junit.Test;
import org.talend.swtbot.test.commons.ContextMenuHelper;
import org.talend.swtbot.test.commons.TalendSwtbotForTdq;
import org.talend.swtbot.test.commons.TalendSwtbotTdqCommon;
import org.talend.swtbot.test.commons.TalendSwtbotTdqCommon.TalendAnalysisTypeEnum;
import org.talend.swtbot.test.commons.TalendSwtbotTdqCommon.TalendMetadataTypeEnum;

public class ColumnAnlaysisAboutMysqlDbconnectionViewTest extends TalendSwtbotForTdq{
	
	@Before
	public void beforeClass(){
		TalendSwtbotTdqCommon.createConnection(bot,
				TalendMetadataTypeEnum.MYSQL);
		bot.editorByTitle(TalendMetadataTypeEnum.MYSQL.toString()+" 0.1").close();
		TalendSwtbotTdqCommon
				.createAnalysis(bot, TalendAnalysisTypeEnum.COLUMN);
	}
	@Test
	public void ColumnAnlaysisAboutMysqlDbconnectionView(){
		String column = TalendSwtbotTdqCommon.getViewColumns(bot,
				TalendMetadataTypeEnum.MYSQL, "tbi", "view_customer", "address1")[0];
		bot.editorByTitle(TalendAnalysisTypeEnum.COLUMN.toString() + " 0.1")
				.show();
		formBot.hyperlink("Select columns to analyze").click();
		bot.waitUntil(Conditions.shellIsActive("Column Selection"));
		SWTBotTree tree = new SWTBotTree((Tree) bot.widget(WidgetOfType
				.widgetOfType(Tree.class)));
		tree.expandNode("tbi").getNode(1).expand().select("view_customer");
		
		bot.table().getTableItem(column).check();
		bot.button("OK").click();
		formBot.ccomboBox(1).setSelection("Nominal");
		bot.toolbarButtonWithTooltip("Save").click();
		formBot.hyperlink("Select indicators for each column").click();
		bot.waitUntil(Conditions.shellIsActive("Indicator Selection"));

		tree = new SWTBotTree((Tree) bot.widget(WidgetOfType
				.widgetOfType(Tree.class)));
		tree.getTreeItem("Simple Statistics").click(1);
		bot.checkBox().click();
		bot.button("OK").click();
		bot.toolbarButtonWithTooltip("Run").click();

		try {
			SWTBotShell shell = bot.shell("Run Analysis");
			bot.waitUntil(Conditions.shellCloses(shell));
		} catch (TimeoutException e) {
		}
		
		bot.viewByTitle("DQ Repository").setFocus();
		tree = new SWTBotTree((Tree) bot.widget(
				WidgetOfType.widgetOfType(Tree.class),
				bot.viewByTitle("DQ Repository").getWidget()));
		tree.expandNode("Metadata","DB connections",TalendMetadataTypeEnum.MYSQL.toString(),"tbi").getNode(1).select();
		ContextMenuHelper.clickContextMenu(tree, "Reload view list");
		bot.waitUntil(Conditions.shellIsActive("Confirm reload"));
		bot.button("OK").click();
		SWTBotShell shell = bot.shell("Progress Information");
//		bot.waitUntil(Conditions.shellCloses(shell));

		bot.waitUntil(Conditions.shellIsActive("Analyzed element changed"));
		bot.button("OK").click();
		bot.waitUntil(Conditions.shellCloses(shell));
		tree.expandNode("Metadata","DB connections",TalendMetadataTypeEnum.MYSQL.toString(),"tbi").getNode(1).expand().
		getNode("view_customer").expand().getNode(0).select();
		ContextMenuHelper.clickContextMenu(tree, "Reload column list");
		bot.waitUntil(Conditions.shellIsActive("Confirm reload"));
		bot.button("OK").click();
		bot.waitUntil(Conditions.shellIsActive("Analyzed element changed"));
		bot.button("OK").click();
		bot.waitUntil(Conditions.shellCloses(shell));
		bot.editorByTitle(TalendAnalysisTypeEnum.COLUMN.toString() + " 0.1").show();
		bot.toolbarButtonWithTooltip("Run").click();

//		try {
//			SWTBotShell shell1 = bot.shell("Run Analysis");
//			bot.waitUntil(Conditions.shellCloses(shell1));
//		} catch (TimeoutException e) {
//		}
		bot.editorByTitle(TalendAnalysisTypeEnum.COLUMN.toString()+" 0.1").close();
		
	}

}
