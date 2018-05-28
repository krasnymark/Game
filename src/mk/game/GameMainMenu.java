package mk.game;

import mk.game.common.model.Game;
import mk.game.common.model.IGame;
import mk.game.common.view.GameView;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Displays a list of games to play. Entry point.
 */
public class GameMainMenu implements ActionListener
{
	List<IGame> games = new ArrayList<IGame>();
    Map<String, IGame> gameMap = new HashMap<String, IGame>();

	public static void main(String[] args)
	{
//		GameMainMenu menu = new GameMainMenu();
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("gameContext.xml") ;
		GameMainMenu menu = (GameMainMenu) context.getBean("mainMenu") ;
		menu.run();
	}

	public void run()
	{
		try
		{
			Game.printInfo("Starting...");
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(games.size() + 1, 1));
            for (IGame game : games)
            {
                String gameName = game.getName();
                gameMap.put(gameName, game);
                JButton gameButton = getGameButton(gameName);
                panel.add(gameButton);
                gameButton.addActionListener(this);
            }
			JButton exit = getGameButton("Exit");
			panel.add(exit);
			GameView.setLargerFont(panel);
			exit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Game.printInfo("Exiting...");
					System.exit(0);
				}
			});
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle("My games");
			frame.getContentPane().add(panel);
			frame.pack();
			frame.setVisible(true);
		}
		catch (Exception ex)
		{
            ex.printStackTrace();
			Game.printInfo(ex.getMessage());
		}
	}

	private JButton getGameButton(String name)
	{
		java.net.URL imgURL = getClass().getResource("/images/" + name.toLowerCase() + ".jpg");
		return new JButton(name, new ImageIcon(imgURL));
	}

	public void actionPerformed(ActionEvent e)
	{
		String gameName = e.getActionCommand();
		playGame(gameName);
	}

	public void playGame(String name)
	{
		IGame game = gameMap.get(name);
		if (!game.isInitialized())
		{
            game.startNewGame();
		}
		else
		{
            game.showGame();
//			JFrame frame = (JFrame) game.getView().getParentContainer();
//			frame.setVisible(true);
//			frame.toFront();
		}
	}

    public List<IGame> getGames()
    {
        return games;
    }

    public void setGames(List<IGame> games)
    {
        this.games = games;
    }
}