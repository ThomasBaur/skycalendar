package skycalendar;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

/**
 * This class provides a demo application for the SkyCalendar.
 */
public class TestCalendar extends JFrame {
	/* Variables declaration */
	private JPanel pnlMain;
	private JButton btnYearDec;
	private JLabel lblYear;
	private JButton btnYearInc;
	private JComboBox<String> cmbMonth;
	private CalendarPanel pnlCalendar;
	private JCheckBox chkSun;
	private JCheckBox chkNautical;
	private JCheckBox chkAstronomical;
	private JCheckBox chkMoon;
	private JCheckBox chkCustom;
	private JCheckBox chkGrid;
	private JLabel lblLongitude;
	private JLabel lblLatitude;
	private JButton btnChngLocation;
	private JComboBox<String> cmbCustom;

	private int year = 2000;
	private GridBagConstraints gridBagConstraints1_1;
	private GridBagConstraints gridBagConstraints1_2;
	private GridBagConstraints gridBagConstraints1_3;
	private GridBagConstraints gridBagConstraints1_4;
	private GridBagConstraints gridBagConstraints1_5;
	private GridBagConstraints gridBagConstraints1_6;
	private GridBagConstraints gridBagConstraints1_7;
	private GridBagConstraints gridBagConstraints1_8;
	private GridBagConstraints gridBagConstraints1_9;
	private GridBagConstraints gridBagConstraints1_10;
	private GridBagConstraints gridBagConstraints1_11;
	private GridBagConstraints gridBagConstraints1_12;
	private GridBagConstraints gridBagConstraints1_13;
	private GridBagConstraints gridBagConstraints1_14;
	private JComboBox<String> cmbTimeZone;

	/**
	 * Creates new form TestCalendar
	 */
	public TestCalendar() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (UnsupportedLookAndFeelException exc) {
			System.out.println("UnsupportedLookAndFeelException: "
					+ exc.getLocalizedMessage());
		} catch (Exception exc) {
			System.out.println("Exception: " + exc.getLocalizedMessage());
		}
		initComponents();
		pack();
		for (String timeZoneId : TimeZone.getAvailableIDs()) {
			cmbTimeZone.addItem(timeZoneId);
		}
		cmbTimeZone.setSelectedItem("GMT0");

		cmbMonth.addItem("Januar");
		cmbMonth.addItem("Februar");
		cmbMonth.addItem("März");
		cmbMonth.addItem("April");
		cmbMonth.addItem("Mai");
		cmbMonth.addItem("Juni");
		cmbMonth.addItem("Juli");
		cmbMonth.addItem("August");
		cmbMonth.addItem("September");
		cmbMonth.addItem("Oktober");
		cmbMonth.addItem("November");
		cmbMonth.addItem("Dezember");

		GregorianCalendar cal = new GregorianCalendar();
		cmbMonth.setSelectedIndex(cal.get(Calendar.MONTH));
		year = cal.get(Calendar.YEAR);
		lblYear.setText(" " + Integer.toString(year) + " ");
		chkMoon.setSelected(pnlCalendar.isShowMoon());
		chkSun.setSelected(pnlCalendar.isShowSun());
		chkNautical.setSelected(pnlCalendar.isShowNaut());
		chkAstronomical.setSelected(pnlCalendar.isShowAstro());
		chkCustom.setSelected(pnlCalendar.isShowCustom());
		chkGrid.setSelected(pnlCalendar.isShowGrid());
		cmbCustom.addItem("Polaris");
		cmbCustom.addItem("M57");
		cmbCustom.addItem("Antares");
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		pnlMain = new JPanel();
		btnYearDec = new JButton();
		lblYear = new JLabel();
		btnYearInc = new JButton();
		cmbMonth = new JComboBox<String>();
		pnlCalendar = new CalendarPanel();
		chkSun = new JCheckBox();
		chkNautical = new JCheckBox();
		chkAstronomical = new JCheckBox();
		chkCustom = new JCheckBox();
		chkMoon = new JCheckBox();
		chkGrid = new JCheckBox();
		lblLongitude = new JLabel();
		lblLatitude = new JLabel();
		btnChngLocation = new JButton();
		cmbCustom = new JComboBox<String>();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				exitForm(evt);
			}
		});

		GridBagLayout gbl_pnlMain = new GridBagLayout();
		gbl_pnlMain.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
		pnlMain.setLayout(gbl_pnlMain);
		GridBagConstraints gridBagConstraints1;

		btnYearDec.setText("<");
		btnYearDec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnYearDecAction(evt);
			}
		});

		gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.insets = new Insets(6, 6, 5, 5);
		gridBagConstraints1.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(btnYearDec, gridBagConstraints1);

		lblYear.setBorder(new BevelBorder(1));
		lblYear.setText("2000");

		gridBagConstraints1_2 = new GridBagConstraints();
		gridBagConstraints1_2.gridx = 1;
		gridBagConstraints1_2.gridy = 0;
		gridBagConstraints1_2.fill = GridBagConstraints.BOTH;
		gridBagConstraints1_2.insets = new Insets(6, 0, 5, 5);
		pnlMain.add(lblYear, gridBagConstraints1_2);

		btnYearInc.setText(">");
		btnYearInc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnYearIncAction(evt);
			}
		});

		gridBagConstraints1_3 = new GridBagConstraints();
		gridBagConstraints1_3.gridx = 2;
		gridBagConstraints1_3.gridy = 0;
		gridBagConstraints1_3.insets = new Insets(6, 0, 5, 5);
		gridBagConstraints1_3.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(btnYearInc, gridBagConstraints1_3);

		cmbMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cmbMonthAction(evt);
			}
		});

		gridBagConstraints1_4 = new GridBagConstraints();
		gridBagConstraints1_4.gridx = 3;
		gridBagConstraints1_4.gridy = 0;
		gridBagConstraints1_4.gridwidth = 0;
		gridBagConstraints1_4.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints1_4.insets = new Insets(6, 3, 5, 6);
		gridBagConstraints1_4.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(cmbMonth, gridBagConstraints1_4);

		pnlCalendar.setBorder(new EtchedBorder());

		gridBagConstraints1_5 = new GridBagConstraints();
		gridBagConstraints1_5.gridx = 0;
		gridBagConstraints1_5.gridy = 1;
		gridBagConstraints1_5.gridwidth = 4;
		gridBagConstraints1_5.gridheight = 11;
		gridBagConstraints1_5.fill = GridBagConstraints.BOTH;
		gridBagConstraints1_5.insets = new Insets(3, 6, 6, 5);
		gridBagConstraints1_5.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints1_5.weightx = 1.0;
		gridBagConstraints1_5.weighty = 1.0;
		pnlMain.add(pnlCalendar, gridBagConstraints1_5);

		chkSun.setText("Sonne");
		chkSun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chkSunAction(evt);
			}
		});

		gridBagConstraints1_6 = new GridBagConstraints();
		gridBagConstraints1_6.gridx = 4;
		gridBagConstraints1_6.gridy = 1;
		gridBagConstraints1_6.gridwidth = 0;
		gridBagConstraints1_6.insets = new Insets(3, 3, 5, 6);
		gridBagConstraints1_6.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(chkSun, gridBagConstraints1_6);

		chkNautical.setText("Nautische Dämmerung");
		chkNautical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chkNauticalAction(evt);
			}
		});

		gridBagConstraints1_7 = new GridBagConstraints();
		gridBagConstraints1_7.gridx = 4;
		gridBagConstraints1_7.gridy = 2;
		gridBagConstraints1_7.gridwidth = 0;
		gridBagConstraints1_7.insets = new Insets(3, 3, 5, 6);
		gridBagConstraints1_7.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(chkNautical, gridBagConstraints1_7);

		chkAstronomical.setText("Astronomische Dämmerung");
		chkAstronomical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chkAstronomicalAction(evt);
			}
		});

		gridBagConstraints1_8 = new GridBagConstraints();
		gridBagConstraints1_8.gridx = 4;
		gridBagConstraints1_8.gridy = 3;
		gridBagConstraints1_8.gridwidth = 0;
		gridBagConstraints1_8.insets = new Insets(3, 3, 5, 6);
		gridBagConstraints1_8.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(chkAstronomical, gridBagConstraints1_8);

		chkCustom.setText("Anderes Objekt");
		chkCustom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chkCustomAction(evt);
			}
		});

		gridBagConstraints1_9 = new GridBagConstraints();
		gridBagConstraints1_9.gridx = 4;
		gridBagConstraints1_9.gridy = 4;
		gridBagConstraints1_9.gridwidth = 0;
		gridBagConstraints1_9.insets = new Insets(3, 3, 5, 6);
		gridBagConstraints1_9.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(chkCustom, gridBagConstraints1_9);

		chkMoon.setText("Mond");
		chkMoon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chkMoonAction(evt);
			}
		});

		gridBagConstraints1_10 = new GridBagConstraints();
		gridBagConstraints1_10.gridx = 4;
		gridBagConstraints1_10.gridy = 5;
		gridBagConstraints1_10.gridwidth = 0;
		gridBagConstraints1_10.insets = new Insets(3, 3, 5, 6);
		gridBagConstraints1_10.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(chkMoon, gridBagConstraints1_10);

		chkGrid.setText("Gitter");
		chkGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chkGridAction(evt);
			}
		});

		gridBagConstraints1_11 = new GridBagConstraints();
		gridBagConstraints1_11.gridx = 4;
		gridBagConstraints1_11.gridy = 6;
		gridBagConstraints1_11.gridwidth = 0;
		gridBagConstraints1_11.insets = new Insets(3, 3, 5, 6);
		gridBagConstraints1_11.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(chkGrid, gridBagConstraints1_11);

		gridBagConstraints1_12 = new GridBagConstraints();
		gridBagConstraints1_12.gridx = 4;
		gridBagConstraints1_12.gridy = 7;
		gridBagConstraints1_12.gridwidth = 0;
		gridBagConstraints1_12.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1_12.insets = new Insets(3, 3, 5, 6);
		gridBagConstraints1_12.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(lblLongitude, gridBagConstraints1_12);

		gridBagConstraints1_13 = new GridBagConstraints();
		gridBagConstraints1_13.gridx = 4;
		gridBagConstraints1_13.gridy = 8;
		gridBagConstraints1_13.gridwidth = 0;
		gridBagConstraints1_13.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1_13.insets = new Insets(3, 3, 5, 6);
		gridBagConstraints1_13.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(lblLatitude, gridBagConstraints1_13);

		btnChngLocation.setText("Ort...");
		btnChngLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnChngLocationAction(evt);
			}
		});

		gridBagConstraints1_14 = new GridBagConstraints();
		gridBagConstraints1_14.gridx = 4;
		gridBagConstraints1_14.gridy = 9;
		gridBagConstraints1_14.gridwidth = 0;
		gridBagConstraints1_14.insets = new Insets(3, 3, 5, 6);
		gridBagConstraints1_14.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(btnChngLocation, gridBagConstraints1_14);

		cmbCustom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cmbCustomAction(evt);
			}
		});

		cmbTimeZone = new JComboBox<String>();
		GridBagConstraints gbc_cmbTimeZone = new GridBagConstraints();
		gbc_cmbTimeZone.anchor = GridBagConstraints.NORTHWEST;
		gbc_cmbTimeZone.insets = new Insets(3, 3, 3, 6);
		gbc_cmbTimeZone.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbTimeZone.gridx = 4;
		gbc_cmbTimeZone.gridy = 10;
		pnlMain.add(cmbTimeZone, gbc_cmbTimeZone);

		cmbTimeZone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cmbTimeZoneAction(evt);
			}
		});

		gridBagConstraints1_1 = new GridBagConstraints();
		gridBagConstraints1_1.gridx = 4;
		gridBagConstraints1_1.gridy = 11;
		gridBagConstraints1_1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1_1.gridwidth = 0;
		gridBagConstraints1_1.insets = new Insets(3, 3, 3, 6);
		gridBagConstraints1_1.anchor = GridBagConstraints.NORTHWEST;
		pnlMain.add(cmbCustom, gridBagConstraints1_1);

		getContentPane().add(pnlMain, BorderLayout.CENTER);

	}

	private void btnChngLocationAction(ActionEvent evt) {
	}

	private void chkGridAction(ActionEvent evt) {
		pnlCalendar.setShowGrid(chkGrid.isSelected());
	}

	private void chkMoonAction(ActionEvent evt) {
		pnlCalendar.setShowMoon(chkMoon.isSelected());
	}

	private void chkCustomAction(ActionEvent evt) {
		pnlCalendar.setShowCustom(chkCustom.isSelected());
	}

	private void chkAstronomicalAction(ActionEvent evt) {
		pnlCalendar.setShowAstro(chkAstronomical.isSelected());
	}

	private void chkNauticalAction(ActionEvent evt) {
		pnlCalendar.setShowNaut(chkNautical.isSelected());
	}

	private void chkSunAction(ActionEvent evt) {
		pnlCalendar.setShowSun(chkSun.isSelected());
	}

	private void cmbMonthAction(ActionEvent evt) {
		setNewDate();
	}

	private void cmbTimeZoneAction(ActionEvent evt) {
		pnlCalendar.setDesiredTimeZone(TimeZone
				.getTimeZone((String) cmbTimeZone.getSelectedItem()));
	}

	private void cmbCustomAction(ActionEvent evt) {
		SkyCoord coord = null;
		int ix = cmbCustom.getSelectedIndex();
		switch (ix) {
		case 0:
			coord = new SkyCoord();
			coord.setRightAscension(1.54);
			coord.setDeclination(89);
			break;

		case 1:
			coord = new SkyCoord();
			coord.setRightAscension(18, 53, 36);
			coord.setDeclination(33, 2, 0);
			break;
		case 2:
			coord = new SkyCoord();
			coord.setRightAscension(16, 29, 24.4);
			coord.setDeclination(-26, 25, 55);
			break;
		}
		if (coord != null) {
			SkyObject obj = new SkyObject(coord);
			pnlCalendar.setCustom(obj);
		}

	}

	private void btnYearIncAction(ActionEvent evt) {
		year++;
		lblYear.setText(" " + Integer.toString(year) + " ");
		setNewDate();
	}

	private void btnYearDecAction(ActionEvent evt) {
		year--;
		lblYear.setText(" " + Integer.toString(year) + " ");
		setNewDate();
	}

	private void setNewDate() {
		int month = cmbMonth.getSelectedIndex();
		if (month >= 0) {
			GregorianCalendar cal = new GregorianCalendar(year, month, 1);
			cal.setTimeZone(TimeZone.getTimeZone("GMT0"));
			pnlCalendar.setDate(cal);
		}
	}

	/**
	 * Exit the Application
	 */
	private void exitForm(WindowEvent evt) {
		System.exit(0);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		new TestCalendar().setVisible(true);
	}

}
