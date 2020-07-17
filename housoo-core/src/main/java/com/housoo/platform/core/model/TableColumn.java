package com.housoo.platform.core.model;

import java.io.Serializable;

/**
 * 描述 数据库表列信息
 *
 * @author housoo
 * @created 2017年1月26日 下午2:01:39
 */
public class TableColumn implements Serializable {

    /**
     * 字符串类型
     */
    public static final int COLUMN_TYPE_STRING = 1;
    /**
     * 数字类型
     */
    public static final int COLUMN_TYPE_NUMBER = 2;
    /**
     * 日期类型
     */
    public static final int COLUMN_TYPE_DATE = 3;
    /**
     * 时间戳类型
     */
    public static final int COLUMN_TYPE_TIMESTAMP = 4;
    /**
     * 大文本类型
     */
    public static final int COLUMN_TYPE_TEXT = 5;
    /**
     * 整型
     */
    public static final int COLUMN_TYPE_INT = 6;

    /**
     * 列ID
     */
    private String columnId;

    /**
     * 列名称
     */
    private String columnName;
    /**
     * 列注释
     */
    private String columnComments;
    /**
     * 列类型(1:字符串 2:数字 3:日期 4:时间戳 5:大文本 6:整型)
     */
    private int columnType;
    /**
     * 当列为字符串类型的时候,它的长度
     */
    private String dataLength;
    /**
     * 是否允许空
     * 1:允许 -1:不允许
     */
    private String isNullable;
    /**
     * 缺省值
     */
    private String defaultValue;
    /**
     * 是否主键(1:是 -1:否)
     */
    private String isPrimaryKey;
    /**
     * 是否唯一性(1:是 -1:否)
     */
    private String isUnique;
    /**
     * 是否外键(1:是 -1:否)
     */
    private String isForeign;
    /**
     * 外键引用表名称
     */
    private String foreignRefTableName;
    /**
     * 外键应用表的列名称
     */
    private String foreignRefColumnName;
    /**
     * 旧列名称
     */
    private String oldColumnName;
    /**
     * 是否是新增的列 1:是 -1:不是
     */
    private String isNewColumn;
    /**
     * 所处的表名称
     */
    private String tableName;
    /**
     * 数字类型精度
     */
    private String precision;
    /**
     * 小数点位数
     */
    private String scale;

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @param columnName the columnName to set
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * @return the columnComments
     */
    public String getColumnComments() {
        return columnComments;
    }

    /**
     * @param columnComments the columnComments to set
     */
    public void setColumnComments(String columnComments) {
        this.columnComments = columnComments;
    }

    /**
     * @return the columnType
     */
    public int getColumnType() {
        return columnType;
    }

    /**
     * @param columnType the columnType to set
     */
    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    /**
     * @return the dataLength
     */
    public String getDataLength() {
        return dataLength;
    }

    /**
     * @param dataLength the dataLength to set
     */
    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    /**
     * @return the isNullable
     */
    public String getIsNullable() {
        return isNullable;
    }

    /**
     * @param isNullable the isNullable to set
     */
    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    /**
     * @return the isPrimaryKey
     */
    public String getIsPrimaryKey() {
        return isPrimaryKey;
    }

    /**
     * @param isPrimaryKey the isPrimaryKey to set
     */
    public void setIsPrimaryKey(String isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    /**
     * @return the isUnique
     */
    public String getIsUnique() {
        return isUnique;
    }

    /**
     * @param isUnique the isUnique to set
     */
    public void setIsUnique(String isUnique) {
        this.isUnique = isUnique;
    }

    /**
     * @return the isForeign
     */
    public String getIsForeign() {
        return isForeign;
    }

    /**
     * @param isForeign the isForeign to set
     */
    public void setIsForeign(String isForeign) {
        this.isForeign = isForeign;
    }

    /**
     * @return the foreignRefTableName
     */
    public String getForeignRefTableName() {
        return foreignRefTableName;
    }

    /**
     * @param foreignRefTableName the foreignRefTableName to set
     */
    public void setForeignRefTableName(String foreignRefTableName) {
        this.foreignRefTableName = foreignRefTableName;
    }

    /**
     * @return the foreignRefColumnName
     */
    public String getForeignRefColumnName() {
        return foreignRefColumnName;
    }

    /**
     * @param foreignRefColumnName the foreignRefColumnName to set
     */
    public void setForeignRefColumnName(String foreignRefColumnName) {
        this.foreignRefColumnName = foreignRefColumnName;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the oldColumnName
     */
    public String getOldColumnName() {
        return oldColumnName;
    }

    /**
     * @param oldColumnName the oldColumnName to set
     */
    public void setOldColumnName(String oldColumnName) {
        this.oldColumnName = oldColumnName;
    }

    /**
     * @return the isNewColumn
     */
    public String getIsNewColumn() {
        return isNewColumn;
    }

    /**
     * @param isNewColumn the isNewColumn to set
     */
    public void setIsNewColumn(String isNewColumn) {
        this.isNewColumn = isNewColumn;
    }

    /**
     * @return the columnId
     */
    public String getColumnId() {
        return columnId;
    }

    /**
     * @param columnId the columnId to set
     */
    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    /**
     * @return the precision
     */
    public String getPrecision() {
        return precision;
    }

    /**
     * @param precision the precision to set
     */
    public void setPrecision(String precision) {
        this.precision = precision;
    }

    /**
     * @return the scale
     */
    public String getScale() {
        return scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(String scale) {
        this.scale = scale;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((columnComments == null) ? 0 : columnComments.hashCode());
        result = prime * result
                + ((columnName == null) ? 0 : columnName.hashCode());
        result = prime * result + columnType;
        result = prime * result
                + ((dataLength == null) ? 0 : dataLength.hashCode());
        result = prime * result
                + ((defaultValue == null) ? 0 : defaultValue.hashCode());
        result = prime * result
                + ((isNullable == null) ? 0 : isNullable.hashCode());
        result = prime * result
                + ((isPrimaryKey == null) ? 0 : isPrimaryKey.hashCode());
        result = prime * result
                + ((isUnique == null) ? 0 : isUnique.hashCode());
        result = prime * result + ((scale == null) ? 0 : scale.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TableColumn other = (TableColumn) obj;
        if (columnComments == null) {
            if (other.columnComments != null) {
                return false;
            }
        } else if (!columnComments.equals(other.columnComments)) {
            return false;
        }
        if (columnName == null) {
            if (other.columnName != null) {
                return false;
            }
        } else if (!columnName.equals(other.columnName)) {
            return false;
        }
        if (columnType != other.columnType) {
            return false;
        }
        if (dataLength == null) {
            if (other.dataLength != null) {
                return false;
            }
        } else if (!dataLength.equals(other.dataLength)) {
            return false;
        }
        if (defaultValue == null) {
            if (other.defaultValue != null) {
                return false;
            }
        } else if (!defaultValue.equals(other.defaultValue)) {
            return false;
        }
        if (isNullable == null) {
            if (other.isNullable != null) {
                return false;
            }
        } else if (!isNullable.equals(other.isNullable)) {
            return false;
        }
        if (isPrimaryKey == null) {
            if (other.isPrimaryKey != null) {
                return false;
            }
        } else if (!isPrimaryKey.equals(other.isPrimaryKey)) {
            return false;
        }
        if (isUnique == null) {
            if (other.isUnique != null) {
                return false;
            }
        } else if (!isUnique.equals(other.isUnique)) {
            return false;
        }
        if (scale == null) {
            if (other.scale != null) {
                return false;
            }
        } else if (!scale.equals(other.scale)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TableColumn [columnName=" + columnName + ", columnComments="
                + columnComments + ", columnType=" + columnType
                + ", dataLength=" + dataLength + ", isNullable=" + isNullable
                + ", defaultValue=" + defaultValue + ", isPrimaryKey="
                + isPrimaryKey + ", isUnique=" + isUnique + ", scale=" + scale
                + "]";
    }
}
