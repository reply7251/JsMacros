package xyz.wagyourtail.doclet.tsdoclet.parsers;

import xyz.wagyourtail.StringHelpers;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public class EventParser extends AbstractParser {
    protected final String name;
    protected final boolean cancellable;

    public EventParser(TypeElement type, String name, boolean cancellable) {
        super(type);
        this.name = name;
        this.isPackage = false;
        this.cancellable = cancellable;
    }

    public String getName() {
        return name;
    }

    @Override
    public String genTSInterface() {
        Set<Element> fields = new LinkedHashSet<>();
        Set<Element> methods = new LinkedHashSet<>();

        for (Element el : type.getEnclosedElements()) {
            if (checkModifier(el, false)) {
                switch (el.getKind()) {
                    case FIELD, ENUM_CONSTANT -> fields.add(el);
                    case METHOD -> methods.add(el);
                }
            }
        }

        Map<Name, Set<Element>> methodMap = new LinkedHashMap<>();
        for (Element m : methods) {
            methodMap.computeIfAbsent(m.getSimpleName(), k -> new HashSet<>()).add(m);
        }

        // remove unnecessary object method overrides
        // mainly toString(): string;
        outer:
        for (Map.Entry<Name, Set<Element>> ent : methodMap.entrySet()) {
            if (!objectMethodNames.contains(ent.getKey())) continue;
            for (Element m : ent.getValue()) if (!isObjectMethod(m)) continue outer;
            methods.removeAll(ent.getValue());
        }

        StringBuilder s = new StringBuilder("interface ").append(name).append(" extends BaseEvent");
        if (cancellable) s.append(", Cancellable");
        s.append(" {\n").append(StringHelpers.tabIn(genFields(fields)));
        String m = genMethods(methods);
        if (!m.isEmpty()) s.append("\n").append(StringHelpers.tabIn(m)).append("\n");
        s.append("}");

        return s.toString().replaceAll("\\{[\n ]+\\}", "{}").replaceAll("\n\n\n+", "\n\n");
    }

}
