public class Enemy {
    String name;
    int healthPoints;
    int attackPoints;
    int armor;
    protected int stage;

    public Enemy(String name, int stage) {
        this.name = name;
        this.stage = stage;
        initAttribute();
    }

    public String getName() {
        return name;
    }

    public int getStage() {
        return stage;
    }

    public void initAttribute() {
        switch (stage) {
            case 1:
                setHealthPoints(30);
                setArmor(3);
                setAttackPoints(4);
                break;
            case 2:
                setHealthPoints(50);
                setArmor(4);
                setAttackPoints(7);
                break;
            case 3:
                setHealthPoints(80);
                setArmor(5);
                setAttackPoints(5);
                break;
            case 4:
                setHealthPoints(300);
                setArmor(6);
                setAttackPoints(25);
                break;
                
            default:
                break;
        }
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

    public void setName(String name) {
        this.name = name;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public boolean isAlive() {
        boolean alive = healthPoints > 0 ? true : false;

        return alive;
    }

    public void attack(Character target) {
        int upperBound = 20;
        int lowerBound = 1;
        int range = (upperBound - lowerBound) + 1;

        int armorUpperBound = 13;
        int armorRange = (armorUpperBound - lowerBound) + 1;

        int d13 = (int) (Math.random() * armorRange) + lowerBound;

        int d20 = (int) (Math.random() * range) + lowerBound;

        if (target.getArmor() < d13) {
            System.out.println(name + " berhasil menyerangmu");
            System.err.println("Hasil lemparan: " + d20);
            int totalDamage = d20 + attackPoints;
            System.out.println("Total damage: " + (totalDamage));
            target.takeDamage(totalDamage);
        } else {
            System.out.println(name + " gagal menyerangmu");
        }
    }

    public void takeDamage(int damage) {
        healthPoints -= damage;
    }
}
