package com.solar.academy.handlers;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
public class Category extends StringTree {

    private static final String   ROOT = new String("root");
    private static final Category ROOT_INSTANCE = new Category(ROOT);
    public  static Category build(){ return ROOT_INSTANCE; }

    public Category(){this(ROOT);}
    public Category(String id) {
        super(id);
    }

    public static Category getRoot() {
        return ROOT_INSTANCE;
    }
    public Category addChild(String id) {
        if( findByIdRecursive(id)==null ){
            Category child = new Category(id);
            child.setParent(this);
            children.put(id, child);
            return this;
        }
        else return null;
    }

    static
    public Category findById(String id) {
        return getRoot().findByIdRecursive(id);
    }
    private Category findByIdRecursive(String id) {
        if (this.id.equals(id)) {
            return this;
        } else {
            return children
                    .values().stream().map(
                            child -> ((Category) child).findByIdRecursive(id)
                    )
                    .filter(found -> found != null)
                    .findFirst().orElse(null);
        }
    }

    public static void deleteByid(String id) {
        if(id.equals(ROOT))return;

        var del = findById(id);
        if (del != null) {
            var upper = del.getParent();
            if (upper != null) {
                del.getChildren().values().stream()
                        .peek(child -> child.setParent(upper))
                        .forEach(child -> upper.children.put(child.id, child));
                upper.children.remove(id);
            }
        }
    }
    public static String getPath(String id){
        if(id.equals(ROOT))return ROOT;

        var tag = findById(id);
        String path = ROOT;
        while(!ROOT.equals(tag.id))
        {
            path = path +"/"+ tag.id;
            tag = (Category)tag.getParent();
        }
        return path;
    }
}