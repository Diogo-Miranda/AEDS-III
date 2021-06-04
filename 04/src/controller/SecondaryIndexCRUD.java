package src.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import src.controller.hash.*;

public class SecondaryIndexCRUD<T2 extends RegistroHashExtensivel<T2>> {
  private Constructor<T2> extendsHashConstructorWithParams;
  private Constructor<T2> extendsHashConstructorWithoutParams;
  private HashExtensivel<T2> hashExtensivel;

  public SecondaryIndexCRUD(Constructor<T2> extendsHashConstructorWithoutParams,
      Constructor<T2> extendsHashConstructorWithParams, String file)
      throws Exception, FileNotFoundException, IOException {
    this.extendsHashConstructorWithoutParams = extendsHashConstructorWithoutParams;
    this.extendsHashConstructorWithParams = extendsHashConstructorWithParams;

    String fileD = String.format("%ssecundario.hash_d.db", file);
    String fileC = String.format("%ssecundario.hash_c.db", file);

    hashExtensivel = new HashExtensivel<>(this.extendsHashConstructorWithoutParams, 4, fileD, fileC);
  }

  public boolean create(String email, int ID) throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, Exception {
    // Criar índice secudário no hash extensível com email e ID
    boolean success = hashExtensivel.create(extendsHashConstructorWithParams.newInstance(email, ID));
    return success;
  }

  public int read(String email) throws Exception {
    T2 obj = hashExtensivel.read(email.hashCode());
    int ID = (obj == null) ? -1 : (int) (obj.getValue());
    return ID;
  }

  public boolean delete(String email) throws Exception {
    return hashExtensivel.delete(email.hashCode());
  }

  public boolean update(T2 obj) throws Exception {
    boolean success = hashExtensivel.update(obj); // id | email
    return success;
  }
}