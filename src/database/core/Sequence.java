package database.core;

public class Sequence {
    String prefix;
    int length;
    String sequenceName;

    /**
     * ORACLE
     * CREATE SEQUENCE moneySequence START WITH 1 INCREMENT BY 1;

     CREATE OR REPLACE FUNCTION moneySequenceTest(length in number, prefix in varchar2)
     return varchar
     is result varchar(255);
     BEGIN
     return  prefix || lpad(moneySequence.nextval, length, '0');
     end;

     select moneySequenceTest(6, 'STR') from DUAL;
     */
    public Sequence(String prefix, int length, String sequenceName) {
        setPrefix(prefix);
        setLength(length);
        setSequenceName(sequenceName);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName+"_seq";
    }
}
