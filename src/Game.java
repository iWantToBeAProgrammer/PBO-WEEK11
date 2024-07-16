
/**
 * Game
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Game {

    private Character mainCharacter;
    private Boss boss;
    private HashMap<Integer, ArrayList<Enemy>> enemies;
    private int currentStage;
    private static int totalTurns = 0;
    private int totalPoint;
    private Statement stmt;
    private Connection connection = null;

    public Game() {
        this.enemies = new HashMap<>();
        this.currentStage = 1;
        initEnemy();
        try {
            connection = Koneksi.getKoneksi();
            if (connection != null) {
                stmt = connection.createStatement();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initEnemy() {
        enemies.clear();

        ArrayList<Enemy> enemiesStage1 = new ArrayList<>();
        enemiesStage1.add(new Enemy("Goblin Pejuang", 1));
        enemiesStage1.add(new Enemy("Goblin Pemanah", 1));
        enemiesStage1.add(new Enemy("Goblin Penyihir", 1));
        enemies.put(1, enemiesStage1);

        ArrayList<Enemy> enemiesStage2 = new ArrayList<>();
        enemiesStage2.add(new Enemy("Bandit Pemukul", 2));
        enemiesStage2.add(new Enemy("Bandit Pengintai", 2));
        enemiesStage2.add(new Enemy("Bandit Penyihir", 2));
        enemies.put(2, enemiesStage2);

        ArrayList<Enemy> enemiesStage3 = new ArrayList<>();
        enemiesStage3.add(new Enemy("Zombie", 3));
        enemiesStage3.add(new Enemy("Jerangkong", 3));
        enemiesStage3.add(new Enemy("Lich", 3));
        enemies.put(3, enemiesStage3);

        this.boss = new Boss("Safi'jiiva", 4);
    }

    private void playStage() {
        switch (currentStage) {
            case 1:
                narrativeStage();
                encounterEnemy(1);
                break;
            case 2:
                narrativeStage();
                encounterEnemy(2);
                break;
            case 3:
                narrativeStage();
                encounterEnemy(3);
                break;
            case 4:
                narrativeStage();
                bossFight(boss);
            default:
                break;
        }
    }

    private void narrativeStage() {
        switch (currentStage) {
            case 1:
                System.out.println(
                        "\nAnda menemukan diri Anda di hutan yang gelap dan misterius. Gerombolan goblin tiba-tiba muncul, siap untuk menyerang dengan tombak dan busur.");
                break;
            case 2:
                System.out.println(
                        "\nSetelah melewati hutan, Anda menemukan desa yang hancur. Bandit-bandit jahat merampok dan menghancurkan desa. Anda harus melawan bandit pemukul, pengintai, dan penyihir.");
                break;
            case 3:
                System.out.println(
                        "\nDi malam yang dingin, Anda menemukan kuburan kuno yang terlupakan. Mayat hidup bangkit, termasuk zombi, kerangka, dan Lich yang menakutkan.");
                break;
            case 4:
                System.out.println(
                        "\nTahap Akhir: Akhirnya, Anda mencapai sarang naga. Naga besar menunggu Anda dengan mata menyala dan api di napasnya. Pertarungan terakhir dimulai!");
            default:
                break;
        }
    }

    private void encounterEnemy(int stage) {

        System.out.println("\nTahap " + stage + ": Gelombang Musuh");
        ArrayList<Enemy> currentEnemies = new ArrayList<>(enemies.get(currentStage));
        for (Enemy enemy : currentEnemies) {
            if (enemy.getStage() == stage) {
                System.out.println("Seorang " + enemy.getName() + " muncul!");
            }
        }

        battle(currentEnemies);

        if (mainCharacter.getHealthPoints() > 0) {
            moveOrStay(currentStage);
        }
    }

    private void moveOrStay(int stage) {
        int currentStagePlayed = 0;
        if (currentStagePlayed <= 3) {
            System.out.println("Lanjut petualangan atau ulang stage");
            System.out.println("1. Lanjut (lanjut next stage)");
            System.out.println("2. Ulang (Kamu bisa ulang untuk grinding exp. max restart= 3)");
            Scanner scn = new Scanner(System.in);
            System.out.print("-> ");
            int choice = scn.nextInt();
            if (choice == 1) {
                moveToNextStage();
            } else if (choice == 2) {
                System.out.println("Kamu mengulang stage ini");
                initEnemy();
                currentStagePlayed++;
            } else {
                System.out.println("input tidak valid");
            }
        } else {
            moveToNextStage();
        }
    }

    public void startGame() {
        Scanner scn = new Scanner(System.in);
        System.out.println("Selamat datang di RPG 'Ancaman Naga'!");
        System.out.print("Nama anda: ");
        String nama = scn.nextLine();
        Character character = new Character(nama);
        mainCharacter = character;
        System.out.println("\nHalo " + mainCharacter.name);
        System.out.println(
                "Seekor naga bernama safi'jiiva yang menakutkan telah meneror pedesaan, membakar desa-desa dan menimbun harta karun.");
        System.out.println(
                "Sang pahlawan harus mengalahkan tiga gelombang musuh dan kemudian menghadapi naga dalam pertarungan epik terakhir.");
        System.out.println("Anda adalah seorang " + mainCharacter.characterType
                + " yang bertugas mengalahkan seekor naga yang menakutkan.");
        while (mainCharacter.getHealthPoints() > 0 && currentStage <= 4) {
            playStage();
        }
    }

    public void endGame() {
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        Koneksi.insertRow(mainCharacter.getName(), dateTime, totalPoint);
        ArrayList<ScoreRecord> topScores = Koneksi.topScores();
        ScoreRecord.showList(topScores);
    }

    private void bossFight(Boss boss) {

        System.out.println("\nNaga " + boss.getName() + " Melihat anda dan bersiap untuk menyerang");

        int bossSkill = 1;
        while (boss.getHealthPoints() > 0 && mainCharacter.getHealthPoints() > 0) {
            boolean enemiesTurn = totalTurns % 2 != 0;
            if (enemiesTurn) {
                if (bossSkill % 5 == 0) {
                    System.out.println("Naga " + boss.getName() + " meregenerasi kembali tubuhnya");
                    boss.useSpecialAbility();
                }
                boss.attack(mainCharacter);
                totalTurns++;
                bossSkill++;
            }
            battleChoice(boss);

            if (mainCharacter.getHealthPoints() <= 0) {
                System.out.println("Kamu gagal dalam pertempuran");
                endGame();
            }

            if (boss.getHealthPoints() <= 0) {
                System.out.println("Kamu telah mengalahkan naga " + boss.getName());
                System.out.println(
                        "\nDengan naga yang telah dikalahkan, negeri pun menghela napas lega. Sang pahlawan berdiri dengan penuh kemenangan, perjalanannya penuh keberanian dan kekuatan kini menjadi legenda yang akan diceritakan sepanjang masa. Saat fajar menyingsing, mereka tahu kedamaian telah kembali, dan nama mereka akan dikenang selamanya.");
                totalPoint += 300;
                moveToNextStage();
                endGame();
            }
        }
    }

    private void battleChoice(ArrayList<Enemy> enemies) {
        System.out.println("============================");
        System.out.println("Pilih aksi: ");
        System.out.println("1. Serang");
        System.out.println("2. Regen hp");
        System.out.println("3. Gunakan Skill (Penetrasi Armor)");
        System.out.println("4. Cek Status");
        System.out.println("============================");

        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();
        switch (choice) {
            case 1:

                mainCharacter.attack(enemyChoice(enemies));
                totalTurns++;
                break;

            case 2:
                mainCharacter.regenerate();
                break;

            case 3:
                mainCharacter.useSkill(enemyChoice(enemies));
                break;
            case 4:
                mainCharacter.statusCheck();
            default:
                break;
        }
    }

    private Enemy enemyChoice(ArrayList<Enemy> enemies) {
        Scanner scn = new Scanner(System.in);
        int enemyChosen = -1;
        boolean inputValid = false;

        while (!inputValid) {
            try {

                System.out.println("\nPilih musuh yang kamu ingin serang");
                for (Enemy enemy : enemies) {
                    System.out.println((enemies.indexOf(enemy) + 1) + ". " + enemy.name);

                }
                System.out.print("-> ");
                enemyChosen = scn.nextInt();

                if (enemyChosen < 1 || enemyChosen > enemies.size()) {
                    System.out.println("inputan salah");
                } else {
                    inputValid = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("input salah, masukkan angka.");
                scn.next();
            }
        }

        return enemies.get(enemyChosen - 1);
    }

    private void battleChoice(Boss boss) {
        System.out.println("============================");
        System.out.println("Pilih aksi: ");
        System.out.println("1. Serang");
        System.out.println("2. Regen hp");
        System.out.println("3. Gunakan Skill (Penetrasi Armor)");
        System.out.println("4. Cek Status");
        System.out.println("============================");

        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();
        switch (choice) {
            case 1:
                mainCharacter.attack(boss);
                totalTurns++;
                break;

            case 2:
                mainCharacter.regenerate();
                break;

            case 3:
                mainCharacter.useSkill(boss);
                break;
            case 4:
                mainCharacter.statusCheck();
            default:
                break;
        }
    }

    public void battle(ArrayList<Enemy> enemies) {
        boolean isTrue = true;

        int index = 1;
        while (isTrue) {
            boolean enemiesTurn = totalTurns % 2 != 0;

            if (enemiesTurn) {
                for (int i = 0; i < enemies.size(); i++) {
                    enemies.get(i).attack(mainCharacter);
                }
                totalTurns++;
            }
            try {
                battleChoice(enemies);
            } catch (InputMismatchException e) {
                System.out.println("Input salah");
            }

            if (!mainCharacter.isAlive()) {
                System.out.println("Kamu gagal dalam pertempuran");
                isTrue = false;
                endGame();
            }

            for (int i = 0; i < enemies.size(); i++) {
                if (!enemies.get(i).isAlive()) {
                    System.out.println("Kamu telah mengalahkan " + enemies.get(i).name);
                    enemies.remove(i);
                    mainCharacter.gainExperience(currentStage);
                    totalPoint += 50;
                    if (enemies.isEmpty()) {
                        System.out.println("Kamu berhasil mengalahkan semua musuh\n");
                        isTrue = false;
                        mainCharacter.healthPoints = mainCharacter.maxHp;
                    }
                }
            }
        }
    }

    public void moveToNextStage() {
        currentStage += 1;
    }

    public int getCurrentStage() {
        return currentStage;
    }
}