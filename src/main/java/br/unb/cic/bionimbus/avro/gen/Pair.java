/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package br.unb.cic.bionimbus.avro.gen;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class Pair extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Pair\",\"namespace\":\"br.unb.cic.bionimbus.avro.gen\",\"fields\":[{\"name\":\"first\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"second\",\"type\":\"long\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.String first;
  @Deprecated public long second;

  /**
   * Default constructor.
   */
  public Pair() {}

  /**
   * All-args constructor.
   */
  public Pair(java.lang.String first, java.lang.Long second) {
    this.first = first;
    this.second = second;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return first;
    case 1: return second;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: first = (java.lang.String)value$; break;
    case 1: second = (java.lang.Long)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'first' field.
   */
  public java.lang.String getFirst() {
    return first;
  }

  /**
   * Sets the value of the 'first' field.
   * @param value the value to set.
   */
  public void setFirst(java.lang.String value) {
    this.first = value;
  }

  /**
   * Gets the value of the 'second' field.
   */
  public java.lang.Long getSecond() {
    return second;
  }

  /**
   * Sets the value of the 'second' field.
   * @param value the value to set.
   */
  public void setSecond(java.lang.Long value) {
    this.second = value;
  }

  /** Creates a new Pair RecordBuilder */
  public static br.unb.cic.bionimbus.avro.gen.Pair.Builder newBuilder() {
    return new br.unb.cic.bionimbus.avro.gen.Pair.Builder();
  }
  
  /** Creates a new Pair RecordBuilder by copying an existing Builder */
  public static br.unb.cic.bionimbus.avro.gen.Pair.Builder newBuilder(br.unb.cic.bionimbus.avro.gen.Pair.Builder other) {
    return new br.unb.cic.bionimbus.avro.gen.Pair.Builder(other);
  }
  
  /** Creates a new Pair RecordBuilder by copying an existing Pair instance */
  public static br.unb.cic.bionimbus.avro.gen.Pair.Builder newBuilder(br.unb.cic.bionimbus.avro.gen.Pair other) {
    return new br.unb.cic.bionimbus.avro.gen.Pair.Builder(other);
  }
  
  /**
   * RecordBuilder for Pair instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Pair>
    implements org.apache.avro.data.RecordBuilder<Pair> {

    private java.lang.String first;
    private long second;

    /** Creates a new Builder */
    private Builder() {
      super(br.unb.cic.bionimbus.avro.gen.Pair.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(br.unb.cic.bionimbus.avro.gen.Pair.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing Pair instance */
    private Builder(br.unb.cic.bionimbus.avro.gen.Pair other) {
            super(br.unb.cic.bionimbus.avro.gen.Pair.SCHEMA$);
      if (isValidValue(fields()[0], other.first)) {
        this.first = data().deepCopy(fields()[0].schema(), other.first);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.second)) {
        this.second = data().deepCopy(fields()[1].schema(), other.second);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'first' field */
    public java.lang.String getFirst() {
      return first;
    }
    
    /** Sets the value of the 'first' field */
    public br.unb.cic.bionimbus.avro.gen.Pair.Builder setFirst(java.lang.String value) {
      validate(fields()[0], value);
      this.first = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'first' field has been set */
    public boolean hasFirst() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'first' field */
    public br.unb.cic.bionimbus.avro.gen.Pair.Builder clearFirst() {
      first = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'second' field */
    public java.lang.Long getSecond() {
      return second;
    }
    
    /** Sets the value of the 'second' field */
    public br.unb.cic.bionimbus.avro.gen.Pair.Builder setSecond(long value) {
      validate(fields()[1], value);
      this.second = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'second' field has been set */
    public boolean hasSecond() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'second' field */
    public br.unb.cic.bionimbus.avro.gen.Pair.Builder clearSecond() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public Pair build() {
      try {
        Pair record = new Pair();
        record.first = fieldSetFlags()[0] ? this.first : (java.lang.String) defaultValue(fields()[0]);
        record.second = fieldSetFlags()[1] ? this.second : (java.lang.Long) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
