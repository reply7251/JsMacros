package xyz.wagyourtail.jsmacros.client.api.classes.filter.logical;

import xyz.wagyourtail.jsmacros.client.api.classes.filter.api.IFilter;

/**
 * @author Etheradon
 * @since 1.6.5
 */
public class XorFilter<T> implements IFilter<T> {

    private final IFilter<T> filterOne;
    private final IFilter<T> filterTwo;

    public XorFilter(IFilter<T> filterOne, IFilter<T> filterTwo) {
        this.filterOne = filterOne;
        this.filterTwo = filterTwo;
    }

    @Override
    public Boolean apply(T obj) {
        return filterOne.apply(obj) ^ filterTwo.apply(obj);
    }

    public IFilter<T> getFilterOne() {
        return filterOne;
    }

    public IFilter<T> getFilterTwo() {
        return filterTwo;
    }
    
}
