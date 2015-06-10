package com.itextpdf.model.element;

import com.itextpdf.canvas.color.Color;
import com.itextpdf.core.font.PdfFont;
import com.itextpdf.model.Property;
import com.itextpdf.model.layout.LayoutPosition;
import com.itextpdf.model.renderer.IRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractElement<Type extends AbstractElement> implements IElement<Type> {

    protected IRenderer nextRenderer;
    protected Map<Integer, Object> properties = new HashMap<>();
    protected List<IElement> childElements = new ArrayList<>();

    @Override
    public void setNextRenderer(IRenderer renderer) {
        this.nextRenderer = renderer;
    }

    @Override
    public IRenderer createRendererSubTree() {
        IRenderer rendererRoot = makeRenderer();
        for (IElement child : childElements) {
            rendererRoot.addChild(child.createRendererSubTree());
        }
        return rendererRoot;
    }

    @Override
    public <T extends Type> T setProperty(int propertyKey, Object value) {
        properties.put(propertyKey, value);
        return (T) this;
    }

    @Override
    public <T> T getProperty(int propertyKey) {
        return (T) properties.get(propertyKey);
    }

    @Override
    public <T> T getDefaultProperty(int propertyKey) {
        switch (propertyKey) {
            case Property.MARGIN_TOP:
            case Property.MARGIN_RIGHT:
            case Property.MARGIN_BOTTOM:
            case Property.MARGIN_LEFT:
            case Property.PADDING_TOP:
            case Property.PADDING_RIGHT:
            case Property.PADDING_BOTTOM:
            case Property.PADDING_LEFT:
                return (T) Float.valueOf(0);
            case Property.POSITION:
                return (T)Integer.valueOf(LayoutPosition.STATIC);
            default:
                return null;
        }
    }

    public Float getWidth() {
        return getProperty(Property.WIDTH);
    }

    public Type setWidth(float width) {
        return setProperty(Property.WIDTH, width);
    }

    public Float getHeight() {
        return getProperty(Property.HEIGHT);
    }

    public Type setHeight(float height) {
        return setProperty(Property.HEIGHT, height);
    }

    public Type setRelativePosition(float left, float top, float right, float bottom) {
        return (Type) setProperty(Property.POSITION, LayoutPosition.RELATIVE).
            setProperty(Property.LEFT, left).
            setProperty(Property.RIGHT, right).
            setProperty(Property.TOP, top).
            setProperty(Property.BOTTOM, bottom);
    }

    public Type setFixedPosition(float x, float y) {
        return (Type) setProperty(Property.POSITION, LayoutPosition.FIXED).
            setProperty(Property.X, x).
            setProperty(Property.Y, y);
    }

    public Type setFixedPosition(int pageNumber, float x, float y) {
        return (Type) setFixedPosition(x, y).
               setProperty(Property.PAGE_NUMBER, pageNumber);
    }

//    public Type setAbsolutePosition(float x, float y) {
//        return (Type) setProperty(Property.POSITION, LayoutPosition.ABSOLUTE).
//            setProperty(Property.X, x).
//            setProperty(Property.Y, y);
//    }

    public Type setFont(PdfFont font) {
        return setProperty(Property.FONT, font);
    }

    public Type setFontColor(Color fontColor) {
        return setProperty(Property.FONT_COLOR, fontColor);
    }

    public Type setFontSize(float fontSize) {
        return setProperty(Property.FONT_SIZE, fontSize);
    }

    public Type setBackgroundColor(Color backgroundColor) {
        return setBackgroundColor(backgroundColor, 0, 0, 0, 0);
    }

    public Type setBackgroundColor(Color backgroundColor, float extraLeft, final float extraTop, final float extraRight, float extraBottom) {
        return setProperty(Property.BACKGROUND, new Property.Background(backgroundColor, extraLeft, extraTop, extraRight, extraBottom));
    }

    public Type setBorder(Property.BorderConfig borderConfig) {
        return setProperty(Property.BORDER, borderConfig);
    }

    public Type setBorderTop(Property.BorderConfig borderConfig) {
        return setProperty(Property.BORDER_TOP, borderConfig);
    }

    public Type setBorderRight(Property.BorderConfig borderConfig) {
        return setProperty(Property.BORDER_RIGHT, borderConfig);
    }

    public Type setBorderBottom(Property.BorderConfig borderConfig) {
        return setProperty(Property.BORDER_BOTTOM, borderConfig);
    }

    public Type setBorderLeft(Property.BorderConfig borderConfig) {
        return setProperty(Property.BORDER_LEFT, borderConfig);
    }

    public Type setAlignment(Property.Alignment alignment) {
        return setProperty(Property.ALIGNMENT, alignment);
    }

    @Override
    public boolean isBreakable() {
        return true;
    }
}