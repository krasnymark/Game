/*
 * Created on May 19, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mk.game.common.view;

import mk.game.common.model.*;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author mk
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class GameMenu implements ChangeListener // ActionListener
{
    protected IGame game;
    protected Action newGameAction, pauseAction, saveAction, exitAction
            , beginnerAction, intermediateAction, advancedAction
            , takeBackAction, goFirstAction, replayAction, hintAction, moveNowAction
            , settingsAction, helpAction, rulesAction;
    protected List<Action> levelAction = new ArrayList<Action>();
    protected JCheckBoxMenuItem pauseMenuItem;

    abstract class GameAction extends AbstractAction
    {
        public GameAction(String text, ImageIcon icon, String desc, Integer mnemonic)
        {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public GameAction(String text, String desc, Integer mnemonic)
        {
            this(text, null, desc, mnemonic);
        }

        public GameAction(String text)
        {
            super(text);
        }

        public GameAction()
        {
            super();
        }
    }

    class NewGameAction extends GameAction
    {
        public NewGameAction(String text)
        {
            super(text);
        }

        public void actionPerformed(ActionEvent e)
        {
            game.startNewGame();
        }
    }

    class SaveAction extends GameAction
    {
        public SaveAction(String text)
        {
            super(text);
        }

        public void actionPerformed(ActionEvent e)
        {
            game.save();

        }
    }

    class PauseAction extends GameAction
    {
        public PauseAction(String text)
        {
            super(text);
        }

        public void actionPerformed(ActionEvent e)
        {
            game.pause();
        }
    }

    class GoFirstAction extends GameAction
    {
        public GoFirstAction(String text)
        {
            super(text);
        }

        public void actionPerformed(ActionEvent e)
        {
            game.goFirst();
        }
    }

    public class ExitAction extends GameAction
    {
        public ExitAction(String text)
        {
            super(text);
        }

        public void actionPerformed(ActionEvent e)
        {
            game.exit();
        }
    }

    class LevelAction extends GameAction
    {
        Game.PlayerLevel level;

        public LevelAction(Game.PlayerLevel playerLevel)
        {
            super(playerLevel.toString());
            level = playerLevel;
        }

        public void actionPerformed(ActionEvent e)
        {
            game.setPlayerLevel(level);
        }
    }

    class TakeBackAction extends GameAction
    {
        public TakeBackAction(String text)
        {
            super(text);
        }

        public void actionPerformed(ActionEvent e)
        {
            game.takeBack();
        }
    }

    class ReplayAction extends GameAction
    {
        public ReplayAction(String text)
        {
            super(text);
        }

        public void actionPerformed(ActionEvent e)
        {
            game.replay();
        }
    }

    class HintAction extends GameAction
    {
        public HintAction(String text)
        {
            super(text);
        }

        public void actionPerformed(ActionEvent e)
        {
            game.hint();
        }
    }

    class MoveNowAction extends GameAction
    {
        public MoveNowAction(String text)
        {
            super(text);
        }

        public void actionPerformed(ActionEvent e)
        {
            game.moveNow();
        }
    }

    class SettingsAction extends GameAction
    {
        public SettingsAction(String text)
        {
            super(text);
        }

        public void actionPerformed(ActionEvent e)
        {
            getSettings();
        }
    }

    class HelpAction extends GameAction
    {
        public HelpAction(String text)
        {
            super(text);
        }

        public void actionPerformed(ActionEvent e)
        {
            game.help();
        }
    }

    class RulesAction extends GameAction
    {
        public RulesAction(String text)
        {
            super(text);
        }

        public void actionPerformed(ActionEvent e)
        {
            game.showRules();
        }
    }

    /**
     *
     */
    public GameMenu(Game game)
    {
        super();
        this.game = game;
//		newGameAction = new NewGameAction("New", "Start New Game", KeyEvent.VK_N);
        newGameAction = new NewGameAction("New");
        pauseAction = new PauseAction("Paused");
        goFirstAction = new GoFirstAction("Go First");
        saveAction = new SaveAction("Save");
        exitAction = new ExitAction("Exit");
        for (int level = 0; level < game.getPlayerLevels(); level++)
        {
            levelAction.add(new LevelAction(Game.PlayerLevel.values()[level]));
        }
        takeBackAction = new TakeBackAction("Take Back");
        replayAction = new ReplayAction("Replay");
        hintAction = new HintAction("Hint");
        moveNowAction = new MoveNowAction("Now");
        settingsAction = new SettingsAction("Settings");
        helpAction = new HelpAction("Help");
        rulesAction = new RulesAction("Rules");
        game.addChangeListener(this);
    }

    public JMenuBar createMenuBar()
    {
        JMenuBar menuBar;
        JMenu menu, levelMenu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the Game menu.
        menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);

        menuItem = new JMenuItem(newGameAction);
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menu.add(menuItem);

        menuItem = new JMenuItem(saveAction);
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menu.add(menuItem);

        menuItem = new JMenuItem(goFirstAction);
        menuItem.setMnemonic(KeyEvent.VK_G);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem(settingsAction);
        menuItem.setMnemonic(KeyEvent.VK_S);
//		menuItem.addActionListener(this);
        menu.add(menuItem);

        //Build the Level submenu.
        levelMenu = new JMenu("Level");
        levelMenu.setMnemonic(KeyEvent.VK_L);

        ButtonGroup group = new ButtonGroup();
        boolean firstLevel = true;
        for (int level = 0; level < game.getPlayerLevels(); level++)
        {
            Game.PlayerLevel playerLevel = Game.PlayerLevel.values()[level];
            rbMenuItem = new JRadioButtonMenuItem(levelAction.get(level));
            rbMenuItem.setSelected(firstLevel);
            firstLevel = false;
            rbMenuItem.setMnemonic(playerLevel.toString().toCharArray()[0]);
            Number k = level + 1;
            rbMenuItem.setAccelerator(KeyStroke.getKeyStroke(("" + (level + 1)).toCharArray()[0], ActionEvent.CTRL_MASK));
            group.add(rbMenuItem);
            levelMenu.add(rbMenuItem);
        }

        menu.add(levelMenu);

        menu.addSeparator();

        pauseMenuItem = new JCheckBoxMenuItem(pauseAction);
        pauseMenuItem.setMnemonic(KeyEvent.VK_P);
        pauseMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        menu.add(pauseMenuItem);

        menu.addSeparator();

        menuItem = new JMenuItem(exitAction);
        menuItem.setMnemonic(KeyEvent.VK_X);
        menu.add(menuItem);

        menuBar.add(menu);

        //Build the Move menu.
        menu = new JMenu("Move");
        menu.setMnemonic(KeyEvent.VK_M);

        menuItem = new JMenuItem(takeBackAction);
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        menu.add(menuItem);

        menuItem = new JMenuItem(replayAction);
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        menu.add(menuItem);

        menuItem = new JMenuItem(hintAction);
        menuItem.setMnemonic(KeyEvent.VK_H);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        menu.add(menuItem);

        menuItem = new JMenuItem(moveNowAction);
        menuItem.setMnemonic(KeyEvent.VK_W);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        menu.add(menuItem);

        menuBar.add(menu);

        //Help menu.
        menu = new JMenu("Help");
//        menu.setAction(helpAction);
        menu.setMnemonic(KeyEvent.VK_F1);

        menuItem = new JMenuItem(rulesAction);
        menuItem.setMnemonic(KeyEvent.VK_R);
        menu.add(menuItem);

        menuBar.add(menu);

        return menuBar;
    }

//	public void actionPerformed(ActionEvent e)
//	{
//		JMenuItem source = (JMenuItem) (e.getSource());
//	}

    public abstract GameSettingsView getSettings();

    /**
     * @return
     */
    public IGame getGame()
    {
        return game;
    }

    /**
     * @param game
     */
    public void setGame(IGame game)
    {
        this.game = game;
    }

    public void stateChanged(ChangeEvent e)
    {
        pauseMenuItem.setState(game.isPaused());
        pauseMenuItem.repaint();
    }

}
