package onyeka.is.great.airhockeygustsofwar;

/**
 * Created by John on 4/9/2016.
 */
public class Collision {
    GameObject object1;
    GameObject object2;

    public Collision(GameObject ob1, GameObject ob2)
    {
        object1 = ob1;
        object2 = ob2;
    }

    public boolean checkIfSame(GameObject ob1, GameObject ob2)
    {
        return (ob1.equals(object1) || ob1.equals(object2) && ob2.equals(object1) || ob2.equals(object2));
    }
}
