import java.lang.Math;
import java.util.Scanner;

public class Character {
    String name;
    int healthPoints;
    int attackPoints;
    int maxHp;
    int armor;
    int experiencePoints;
    int expToGain;
    int level = 1;
    String characterType;
    boolean critChance = false;
    private static int skillCooldown = 0;
    private static int regenCooldown = 0;

    public Character(String name) {
        this.name = name;
        maxHp = healthPoints;
        chooseRole();
        initExpToGain();
    }

    public void initExpToGain() {
        if (level == 1) {
            expToGain = 50;
        } else if (level == 2) {
            expToGain = 100;
        } else if (level <= 5) {
            expToGain = 150;
        }
    }

    public void chooseRole() {
        System.out.println("pilih role dan atribut untuk karaktermu");
        System.out.println("===========================================");
        System.out.println("1. Assassin (High Damage, Low hp)");
        System.out.println("2. Giant (Low Damage, High hp)");
        System.out.println("3. Prince (Moderate Damage, Moderate hp)");
        System.out.print("Pilih: ");
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        switch (choice) {
            case 1:
                setCharacterType("Assassin");
                setArmor(4);
                setHealthPoints(100);
                setAttackPoints(20);
                break;
            case 2:
                setCharacterType("Giant");
                setArmor(7);
                setHealthPoints(180);
                setAttackPoints(7);
                break;
            case 3:
                setCharacterType("Prince");
                setArmor(5);
                setHealthPoints(120);
                setAttackPoints(12);
                break;
            default:
                break;
        }
    }

    public String getName() {
        return name;
    }

    public void setCharacterType(String characterType) {
        this.characterType = characterType;
    }

    public String getCharacterType() {
        return characterType;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getArmor() {
        return armor;
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public int getLevel() {
        return level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
        this.maxHp = healthPoints;
    }

    public boolean isAlive() {
        boolean alive = healthPoints > 0 ? true : false;

        return alive;
    }

    public void attack(Enemy target) {
        boolean isEntered = true;
        Scanner inp = new Scanner(System.in);
        int rollArmor = 0;
        while (isEntered) {
            System.out.println("Klik enter untuk menyerang musuh!!");
            System.out.print("->");
            String input = inp.nextLine();
            if (input.equals("")) {
                rollArmor = rollArmor();
                isEntered = false;
            }
        }

        if (target.getArmor() < rollArmor) {
            System.out.println("Seranganmu mengenai musuh, lempar dadu untuk menentukan damage");
            int rollDamage = 0;
            isEntered = true;
            while (isEntered) {
                System.out.println("Klik enter untuk melempar dadu!!");
                System.out.print("->");
                String input = inp.nextLine();
                if (input.equals("")) {
                    rollDamage = rollDamage();
                    isEntered = false;
                }
            }
            System.err.println("Hasil lemparan: " + rollDamage);
            int totalDamage = rollDamage + attackPoints;
            if (critChance) {
                int chance = (int) (Math.random() * 4) + 1;
                if (chance == 1) {
                    System.out.println("Crit Damage!!");
                    totalDamage += totalDamage * 30 / 100;
                }
            }
            System.out.println("Total damage: " + (totalDamage));
            target.takeDamage(totalDamage);
        } else {
            System.out.println("Seranganmu ditahan oleh musuh");
        }

        if (skillCooldown > 0 && regenCooldown > 0) {
            skillCooldown -= 1;
            regenCooldown -= 1;
        }

        if (skillCooldown > 0) {
            skillCooldown -= 1;
        }

        if (regenCooldown > 0) {
            regenCooldown -= 1;
        }
    }

    public int rollArmor() {
        int armorUpperBound = 13;
        int lowerBound = 1;
        int armorRange = (armorUpperBound - lowerBound) + 1;
        int armorDamage = (int) (Math.random() * armorRange) + lowerBound;

        return armorDamage;
    }

    public int rollDamage() {
        int damageUpperBound = 20;
        int lowerBound = 1;
        int damageRange = (damageUpperBound - lowerBound) + 1;
        int rollDamage = (int) (Math.random() * damageRange) + lowerBound;

        return rollDamage;
    }

    public void takeDamage(int damage) {
        healthPoints = healthPoints - damage;
    }

    public void regenerate() {
        if (regenCooldown == 0) {

            healthPoints += 20;
            regenCooldown = 3;

        }
        System.out.println("total hp: " + healthPoints);
        System.out.println("Regen cooldown: " + regenCooldown);
    }

    public void levelUp() {

        if (level < 5) {
            level += 1;
            armor += 1;
            maxHp += 5;
            attackPoints += 5;
            System.out.println("Selamat level anda naik dan mendapatkan tambahan stats");
        } else {
            System.out.println("level anda Max dan mendapatkan hidden buff berupa Crit chance");
            critChance = true;
        }
    }

    public void useSkill(Enemy target) {
        System.out.println("Menggunakan skill penetrasi armor");
        if (skillCooldown == 0) {
            int upperBound = 20;
            int lowerBound = 1;
            int range = (upperBound - lowerBound) + 1;

            int d20 = (int) (Math.random() * range) + lowerBound;
            int totalDamage = d20 + attackPoints;
            System.out.println("Total damage: " + (totalDamage));
            target.takeDamage(totalDamage);
            skillCooldown = 3;

        }

        System.out.println("Skill cooldown : " + skillCooldown);

    }

    public void statusCheck() {
        String skillCheck = skillCooldown == 0 ? "available" : skillCooldown + " turns";
        String regenCheck = regenCooldown == 0 ? "available" : regenCooldown + " turns";

        System.out.println("===============STATUS=============");
        System.out.println("|| nama:\t" + name + "\t\t||");
        System.out.println("|| karakter:\t" + characterType + "\t||");
        System.out.println("|| exp:\t\t" + experiencePoints + "/" + expToGain + "\t\t||");
        System.out.println("|| level:\t" + level + "\t\t||");
        System.out.println("|| hp:\t\t" + healthPoints + "\t\t||");
        System.out.println("|| armor:\t" + armor + "\t\t||");
        System.out.println("|| atk:\t\t" + attackPoints + "\t\t||");
        System.out.println("|| skill:\t" + skillCheck + "\t||");
        System.out.println("|| regen:\t" + regenCheck + "\t||");
        System.out.println("==================================");
    }

    public void gainExperience(int stage) {
        initExpToGain();
        if (stage == 1) {
            experiencePoints += 15;
        } else if (stage == 2) {
            experiencePoints += 30;
        } else if (stage == 3) {
            experiencePoints += 40;
        }

        if (experiencePoints >= expToGain) {
            levelUp();
            experiencePoints = 0;
        }
    }
}
