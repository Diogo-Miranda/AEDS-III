import java.io.IOException;

public interface Registro {
    public int getID();
    public void setID(int ID);
    public byte[] toByteArray() throws IOException;
    public void fromByteArray(byte[] ba) throws IOException;
}