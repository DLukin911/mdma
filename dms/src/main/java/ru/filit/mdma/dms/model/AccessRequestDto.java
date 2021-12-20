package ru.filit.mdma.dms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * Запрос прав доступа для роли
 */
@ApiModel(description = "Запрос прав доступа для роли")
public class AccessRequestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonProperty("role")
  private String role;

  @JsonProperty("version")
  private String version;

  public AccessRequestDto role(String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   *
   * @return role
   */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public AccessRequestDto version(String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   *
   * @return version
   */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccessRequestDto accessRequestDto = (AccessRequestDto) o;
    return Objects.equals(this.role, accessRequestDto.role) &&
        Objects.equals(this.version, accessRequestDto.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(role, version);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccessRequestDto {\n");

    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

