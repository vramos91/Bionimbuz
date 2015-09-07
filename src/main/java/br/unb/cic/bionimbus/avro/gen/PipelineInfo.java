/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package br.unb.cic.bionimbus.avro.gen;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class PipelineInfo extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"PipelineInfo\",\"namespace\":\"br.unb.cic.bionimbus.avro.gen\",\"fields\":[{\"name\":\"id\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"jobs\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"JobInfo\",\"fields\":[{\"name\":\"id\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"localId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"serviceId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"args\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"inputs\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Pair\",\"fields\":[{\"name\":\"first\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"second\",\"type\":\"long\"}]}}},{\"name\":\"outputs\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}},{\"name\":\"timestamp\",\"type\":\"long\"},{\"name\":\"worstExecution\",\"type\":\"double\"}]}}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.String id;
  @Deprecated public java.util.List<br.unb.cic.bionimbus.avro.gen.JobInfo> jobs;

  /**
   * Default constructor.
   */
  public PipelineInfo() {}

  /**
   * All-args constructor.
   */
  public PipelineInfo(java.lang.String id, java.util.List<br.unb.cic.bionimbus.avro.gen.JobInfo> jobs) {
    this.id = id;
    this.jobs = jobs;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return jobs;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.String)value$; break;
    case 1: jobs = (java.util.List<br.unb.cic.bionimbus.avro.gen.JobInfo>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'id' field.
   */
  public java.lang.String getId() {
    return id;
  }

  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.String value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'jobs' field.
   */
  public java.util.List<br.unb.cic.bionimbus.avro.gen.JobInfo> getJobs() {
    return jobs;
  }

  /**
   * Sets the value of the 'jobs' field.
   * @param value the value to set.
   */
  public void setJobs(java.util.List<br.unb.cic.bionimbus.avro.gen.JobInfo> value) {
    this.jobs = value;
  }

  /** Creates a new PipelineInfo RecordBuilder */
  public static br.unb.cic.bionimbus.avro.gen.PipelineInfo.Builder newBuilder() {
    return new br.unb.cic.bionimbus.avro.gen.PipelineInfo.Builder();
  }
  
  /** Creates a new PipelineInfo RecordBuilder by copying an existing Builder */
  public static br.unb.cic.bionimbus.avro.gen.PipelineInfo.Builder newBuilder(br.unb.cic.bionimbus.avro.gen.PipelineInfo.Builder other) {
    return new br.unb.cic.bionimbus.avro.gen.PipelineInfo.Builder(other);
  }
  
  /** Creates a new PipelineInfo RecordBuilder by copying an existing PipelineInfo instance */
  public static br.unb.cic.bionimbus.avro.gen.PipelineInfo.Builder newBuilder(br.unb.cic.bionimbus.avro.gen.PipelineInfo other) {
    return new br.unb.cic.bionimbus.avro.gen.PipelineInfo.Builder(other);
  }
  
  /**
   * RecordBuilder for PipelineInfo instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<PipelineInfo>
    implements org.apache.avro.data.RecordBuilder<PipelineInfo> {

    private java.lang.String id;
    private java.util.List<br.unb.cic.bionimbus.avro.gen.JobInfo> jobs;

    /** Creates a new Builder */
    private Builder() {
      super(br.unb.cic.bionimbus.avro.gen.PipelineInfo.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(br.unb.cic.bionimbus.avro.gen.PipelineInfo.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing PipelineInfo instance */
    private Builder(br.unb.cic.bionimbus.avro.gen.PipelineInfo other) {
            super(br.unb.cic.bionimbus.avro.gen.PipelineInfo.SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.jobs)) {
        this.jobs = data().deepCopy(fields()[1].schema(), other.jobs);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'id' field */
    public java.lang.String getId() {
      return id;
    }
    
    /** Sets the value of the 'id' field */
    public br.unb.cic.bionimbus.avro.gen.PipelineInfo.Builder setId(java.lang.String value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'id' field has been set */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'id' field */
    public br.unb.cic.bionimbus.avro.gen.PipelineInfo.Builder clearId() {
      id = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'jobs' field */
    public java.util.List<br.unb.cic.bionimbus.avro.gen.JobInfo> getJobs() {
      return jobs;
    }
    
    /** Sets the value of the 'jobs' field */
    public br.unb.cic.bionimbus.avro.gen.PipelineInfo.Builder setJobs(java.util.List<br.unb.cic.bionimbus.avro.gen.JobInfo> value) {
      validate(fields()[1], value);
      this.jobs = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'jobs' field has been set */
    public boolean hasJobs() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'jobs' field */
    public br.unb.cic.bionimbus.avro.gen.PipelineInfo.Builder clearJobs() {
      jobs = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public PipelineInfo build() {
      try {
        PipelineInfo record = new PipelineInfo();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.String) defaultValue(fields()[0]);
        record.jobs = fieldSetFlags()[1] ? this.jobs : (java.util.List<br.unb.cic.bionimbus.avro.gen.JobInfo>) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
