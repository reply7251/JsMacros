package xyz.wagyourtail.jsmacros.client.mixins.access;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.wagyourtail.jsmacros.client.api.helpers.ButtonWidgetHelper;
import xyz.wagyourtail.jsmacros.client.api.helpers.ItemStackHelper;
import xyz.wagyourtail.jsmacros.client.api.helpers.TextFieldWidgetHelper;
import xyz.wagyourtail.jsmacros.client.api.helpers.TextHelper;
import xyz.wagyourtail.jsmacros.client.api.sharedclasses.PositionCommon;
import xyz.wagyourtail.jsmacros.client.api.sharedclasses.PositionCommon.Pos2D;
import xyz.wagyourtail.jsmacros.client.api.sharedclasses.PositionCommon.Vec2D;
import xyz.wagyourtail.jsmacros.client.api.sharedclasses.RenderCommon;
import xyz.wagyourtail.jsmacros.client.api.sharedinterfaces.IScreen;
import xyz.wagyourtail.jsmacros.core.Core;
import xyz.wagyourtail.jsmacros.core.MethodWrapper;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Mixin(Screen.class)
public abstract class MixinScreen extends AbstractParentElement implements IScreen {
    @Unique private final Set<Drawable> elements = new LinkedHashSet<>();
    @Unique private MethodWrapper<PositionCommon.Pos2D, Integer, Object> onMouseDown;
    @Unique private MethodWrapper<PositionCommon.Vec2D, Integer, Object> onMouseDrag;
    @Unique private MethodWrapper<PositionCommon.Pos2D, Integer, Object> onMouseUp;
    @Unique private MethodWrapper<PositionCommon.Pos2D, Double, Object> onScroll;
    @Unique private MethodWrapper<Integer, Integer, Object> onKeyPressed;
    @Unique private MethodWrapper<IScreen, Object, Object> onInit;
    @Unique private MethodWrapper<String, Object, Object> catchInit;
    @Unique private MethodWrapper<IScreen, Object, Object> onClose;
    
    @Shadow public int width;
    @Shadow public int height;
    @Shadow @Final protected Text title;
    @Shadow protected MinecraftClient client;
    @Shadow protected TextRenderer textRenderer;
    @Shadow @Final protected List<Element> children;
    
    @Shadow protected abstract <T extends AbstractButtonWidget> T addButton(T button);
    @Shadow public abstract void onClose();
    @Shadow public abstract void init();
    
    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public List<RenderCommon.Text> getTexts() {
        List<RenderCommon.Text> list = new LinkedList<>();
        synchronized (elements) {
            for (Drawable e : elements) {
                if (e instanceof RenderCommon.Text) list.add((RenderCommon.Text) e);
            }
        }
        return list;
    }

    @Override
    public List<RenderCommon.Rect> getRects() {
        List<RenderCommon.Rect> list = new LinkedList<>();
        synchronized (elements) {
            for (Drawable e : elements) {
                if (e instanceof RenderCommon.Rect) list.add((RenderCommon.Rect) e);
            }
        }
        return list;
    }

    @Override
    public List<RenderCommon.Item> getItems() {
        List<RenderCommon.Item> list = new LinkedList<>();
        synchronized (elements) {
            for (Drawable e : elements) {
                if (e instanceof RenderCommon.Item) list.add((RenderCommon.Item) e);
            }
        }
        return list;
    }

    @Override
    public List<RenderCommon.Image> getImages() {
        List<RenderCommon.Image> list = new LinkedList<>();
        synchronized (elements) {
            for (Drawable e : elements) {
                if (e instanceof RenderCommon.Image) list.add((RenderCommon.Image) e);
            }
        }
        return list;
    }

    @Override
    public List<TextFieldWidgetHelper> getTextFields() {
        List<TextFieldWidgetHelper> list = new LinkedList<>();
        synchronized (elements) {
            for (Drawable e : elements) {
                if (e instanceof TextFieldWidgetHelper) list.add((TextFieldWidgetHelper) e);
            }
        }
        return list;
    }
    
    @Override
    public List<ButtonWidgetHelper> getButtonWidgets() {
        List<ButtonWidgetHelper> list = new LinkedList<>();
        synchronized (elements) {
            for (Drawable e : elements) {
                if (e instanceof ButtonWidgetHelper) list.add((ButtonWidgetHelper) e);
            }
        }
        return list;
    }
    
    @Override
    public List<Drawable> getElements() {
        return ImmutableList.copyOf(elements);
    }
    
    @Override
    public IScreen removeElement(Drawable e) {
        synchronized (elements) {
            elements.remove(e);
            if (e instanceof ButtonWidgetHelper) children.remove(((ButtonWidgetHelper) e).getRaw());
        }
        return this;
    }
    
    @Override
    public Drawable reAddElement(Drawable e) {
        synchronized (elements) {
            elements.add(e);
            if (e instanceof ButtonWidgetHelper) children.add(((ButtonWidgetHelper<?>) e).getRaw());
        }
        return e;
    }
    
    @Override
    public RenderCommon.Text addText(String text, int x, int y, int color, boolean shadow) {
        return addText(text, x, y, color, shadow, 1, 0);
    }

    @Override
    public RenderCommon.Text addText(String text, int x, int y, int color, boolean shadow, double scale, float rotation) {
        RenderCommon.Text t = new RenderCommon.Text(text, x, y, color, shadow, scale, rotation);
        synchronized (elements) {
            elements.add(t);
        }
        return t;
    }
    

    @Override
    public RenderCommon.Text addText(TextHelper text, int x, int y, int color, boolean shadow) {
        return addText(text, x, y, color, shadow, 1, 0);
    }

    @Override
    public RenderCommon.Text addText(TextHelper text, int x, int y, int color, boolean shadow, double scale, float rotation) {
        RenderCommon.Text t = new RenderCommon.Text(text, x, y, color, shadow, scale, rotation);
        synchronized (elements) {
            elements.add(t);
        }
        return t;
    }

    @Override
    public IScreen removeText(RenderCommon.Text t) {
        synchronized (elements) {
            elements.remove(t);
        }
        return this;
    }

    @Override
    public RenderCommon.Image addImage(int x, int y, int width, int height, String id, int imageX, int imageY, int regionWidth,
        int regionHeight, int textureWidth, int textureHeight) {
        return addImage(x, y, width, height, id, imageX, imageY, regionWidth, regionHeight, textureWidth, textureHeight, 0);
    }

    @Override
    public RenderCommon.Image addImage(int x, int y, int width, int height, String id, int imageX, int imageY, int regionWidth,
        int regionHeight, int textureWidth, int textureHeight, float rotation) {
        RenderCommon.Image i = new RenderCommon.Image(x, y, width, height, id, imageX, imageY, regionWidth, regionHeight, textureWidth, textureHeight, rotation);
        synchronized (elements) {
            elements.add(i);
        }
        return i;
    }

    @Override
    public IScreen removeImage(RenderCommon.Image i) {
        synchronized (elements) {
            elements.remove(i);
        }
        return this;
    }

    @Override
    public RenderCommon.Rect addRect(int x1, int y1, int x2, int y2, int color) {
        RenderCommon.Rect r = new RenderCommon.Rect(x1, y1, x2, y2, color, 0F);
        synchronized (elements) {
            elements.remove(r);
        }
        return r;
    }

    @Override
    public RenderCommon.Rect addRect(int x1, int y1, int x2, int y2, int color, int alpha) {
        return addRect(x1, y1, x2, y2, color, alpha, 0);
    }

    @Override
    public RenderCommon.Rect addRect(int x1, int y1, int x2, int y2, int color, int alpha, float rotation) {
        RenderCommon.Rect r = new RenderCommon.Rect(x1, y1, x2, y2, color, alpha, rotation);
        synchronized (elements) {
            elements.remove(r);
        }
        return r;
    }

    @Override
    public IScreen removeRect(RenderCommon.Rect r) {
        synchronized (elements) {
            elements.remove(r);
        }
        return this;
    }

    @Override
    public RenderCommon.Item addItem(int x, int y, String id, boolean overlay) {
        return addItem(y, y, id, overlay, 1, 0);
    }

    @Override
    public RenderCommon.Item addItem(int x, int y, String id, boolean overlay, double scale, float rotation) {
        RenderCommon.Item i = new RenderCommon.Item(y, y, id, overlay, scale, rotation);
        synchronized (elements) {
            elements.remove(i);
        }
        return i;
    }

    @Override
    public RenderCommon.Item addItem(int x, int y, ItemStackHelper item, boolean overlay) {
        return addItem(x, y, item, overlay, 1, 0);
    }

    @Override
    public RenderCommon.Item addItem(int x, int y, ItemStackHelper item, boolean overlay, double scale, float rotation) {
        RenderCommon.Item i = new RenderCommon.Item(x, y, item, overlay, scale, rotation);
        synchronized (elements) {
            elements.remove(i);
        }
        return i;
    }

    @Override
    public IScreen removeItem(RenderCommon.Item i) {
        synchronized (elements) {
            elements.remove(i);
        }
        return this;
    }

    @Override
    public String getTitleText() {
        return title.getString();
    }

    @Override
    public ButtonWidgetHelper addButton(int x, int y, int width, int height, String text,
        MethodWrapper<ButtonWidgetHelper, IScreen, Object> callback) {
        ButtonWidget button = (ButtonWidget) new ButtonWidget(x, y, width, height, new LiteralText(text), (btn) -> {
            try {
                callback.accept(new ButtonWidgetHelper(btn), this);
            } catch (Exception e) {
                Core.instance.profile.logError(e);
            }
        });
        ButtonWidgetHelper b = new ButtonWidgetHelper(button);
        synchronized (elements) {
            elements.add(b);
            children.add(button);
        }
        return b;
    }

    @Override
    public IScreen removeButton(ButtonWidgetHelper btn) {
        synchronized (elements) {
            elements.remove(btn);
            this.children.remove(btn.getRaw());
        }
        return this;
    }

    @Override
    public TextFieldWidgetHelper addTextInput(int x, int y, int width, int height, String message,
        MethodWrapper<String, IScreen, Object> onChange) {
        TextFieldWidget field = new TextFieldWidget(this.textRenderer, x, y, width, height, new LiteralText(message));
        if (onChange != null) {
            field.setChangedListener(str -> {
                try {
                    onChange.accept(str, this);
                } catch (Exception e) {
                    Core.instance.profile.logError(e);
                }
            });
        }
        TextFieldWidgetHelper w = new TextFieldWidgetHelper(field);
        synchronized (elements) {
            elements.add(w);
            children.add(field);
        }
        return w;
    }

    @Override
    public IScreen removeTextInput(TextFieldWidgetHelper inp) {
        synchronized (elements) {
            elements.remove(inp);
            children.remove(inp.getRaw());
        }
        return this;
    }

    @Override
    public void close() {
        onClose();
        
    }

    @Override
    public IScreen setOnMouseDown(MethodWrapper<Pos2D, Integer, Object> onMouseDown) {
        this.onMouseDown = onMouseDown;
        return this;
    }

    @Override
    public IScreen setOnMouseDrag(MethodWrapper<Vec2D, Integer, Object> onMouseDrag) {
        this.onMouseDrag = onMouseDrag;
        return this;
    }

    @Override
    public IScreen setOnMouseUp(MethodWrapper<Pos2D, Integer, Object> onMouseUp) {
        this.onMouseUp = onMouseUp;
        return this;
    }

    @Override
    public IScreen setOnScroll(MethodWrapper<Pos2D, Double, Object> onScroll) {
        this.onScroll = onScroll;
        return this;
    }

    @Override
    public IScreen setOnKeyPressed(MethodWrapper<Integer, Integer, Object> onKeyPressed) {
        this.onKeyPressed = onKeyPressed;
        return this;
    }

    @Override
    public IScreen setOnInit(MethodWrapper<IScreen, Object, Object> onInit) {
        this.onInit = onInit;
        return this;
    }

    @Override
    public IScreen setOnFailInit(MethodWrapper<String, Object, Object> catchInit) {
        this.catchInit = catchInit;
        return this;
    }

    @Override
    public IScreen setOnClose(MethodWrapper<IScreen, Object, Object> onClose) {
        this.onClose = onClose;
        return this;
    }
    
    @Override
    public IScreen reloadScreen() {
        client.openScreen((Screen) (Object) this);
        return this;
    }

    @Inject(at = @At("RETURN"), method = "render")
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (matrices == null) return;
        
        synchronized (elements) {
            for (Drawable element : elements) {
                element.render(matrices, mouseX, mouseY, delta);
            }
        }
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (onMouseDown != null) try {
            onMouseDown.accept(new PositionCommon.Pos2D(mouseX, mouseY), button);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (onMouseDrag != null) try {
            onMouseDrag.accept(new PositionCommon.Vec2D(mouseX, mouseY, deltaX, deltaY), button);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (onMouseUp != null) try {
            onMouseUp.accept(new PositionCommon.Pos2D(mouseX, mouseY), button);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Inject(at = @At("HEAD"), method = "keyPressed")
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> info) {
        if (onKeyPressed != null) try {
            onKeyPressed.accept(keyCode, modifiers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (onScroll != null) try {
            onScroll.accept(new PositionCommon.Pos2D(mouseX, mouseY), amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
    
    @Inject(at = @At("RETURN"), method = "init()V")
    protected void init(CallbackInfo info) {
        synchronized (elements) {
        elements.clear();
        }
        client.keyboard.setRepeatEvents(true);
        if (onInit != null) {
            try {
                onInit.accept(this);
            } catch (Exception e) {
                try {
                    if (catchInit != null) catchInit.accept(e.toString());
                    else throw e;
                } catch (Exception f) {
                    Core.instance.profile.logError(f);
                }
            }
        }
    }
    
    @Inject(at = @At("RETURN"), method = "removed")
    public void removed(CallbackInfo info) {
        if (onClose != null) {
            try {
                onClose.accept(this);
            } catch (Exception e) {
                Core.instance.profile.logError(e);
            }
        }
    }
}
