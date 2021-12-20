package ru.filit.mdma.dms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * Права доступа к полям сущностей
 */
@ApiModel(description = "Права доступа к полям сущностей")
public class AccessDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonProperty("entity")
  private String entity;

  @JsonProperty("property")
  private String property;

  public AccessDto entity(String entity) {
    this.entity = entity;
    return this;
  }

  /**
   * Get entity
   *
   * @return entity
   */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  public String getEntity() {
    return entity;
  }

  public void setEntity(String entity) {
    this.entity = entity;
  }

  public AccessDto property(String property) {
    this.property = property;
    return this;
  }

  /**
   * Get property
   *
   * @return property
   */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccessDto accessDto = (AccessDto) o;
    return Objects.equals(this.entity, accessDto.entity) &&
        Objects.equals(this.property, accessDto.property);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entity, property);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccessDto {\n");

    sb.append("    entity: ").append(toIndentedString(entity)).append("\n");
    sb.append("    property: ").append(toIndentedString(property)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first
   * line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

