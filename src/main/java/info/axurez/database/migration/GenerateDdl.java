package info.axurez.database.migration;

import javax.persistence.*;

import info.axurez.database.entities.*;

public class GenerateDdl {
    public static void main(String args[]) {
        Persistence.generateSchema("info.axurez.zhihu", null);
    }
}
