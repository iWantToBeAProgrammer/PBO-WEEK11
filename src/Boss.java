public class Boss extends Enemy{
    public Boss(String name, int stage) {
        super(name, stage);
    }

    public void useSpecialAbility() {
        healthPoints += 50;
    }
    
}
