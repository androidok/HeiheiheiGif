package com.boredream.hhhgif.entity;

import java.util.List;

public class AddRelationOperation extends Operation {

    public AddRelationOperation() {
        set__op(OP_ADD_RELATION);
    }

    /**
     * 添加relation关系的数据集合
     */
    private List<RelationObj> objects;

    public List<RelationObj> getObjects() {
        return objects;
    }

    public void setObjects(List<RelationObj> objects) {
        this.objects = objects;
    }

    /**
     * 数据关系
     */
    public static class RelationObj {

        public static final String POINTER = "Pointer";

        /**
         * 关系类型
         */
        private String __type;

        /**
         * 操作数据类名
         */
        private String className;

        /**
         * 操作数据id
         */
        private String objectId;

        public void set__type(String __type) {
            this.__type = __type;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String get__type() {
            return __type;
        }

        public String getClassName() {
            return className;
        }

        public String getObjectId() {
            return objectId;
        }
    }
}
