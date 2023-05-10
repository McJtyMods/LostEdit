package com.mcjty.lostedit.client.gui;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import mcjty.lib.gui.widgets.*;

import java.util.stream.Stream;

public class GuiOps implements DynamicOps<Widget> {

    @Override
    public Widget empty() {
        return null;
    }

    @Override
    public <U> U convertTo(DynamicOps<U> outOps, Widget input) {
        if (input instanceof TextField text) {
            return outOps.createString(text.getText());
        } else if (input instanceof ToggleButton toggle) {
            return outOps.createBoolean(toggle.isPressed());
        } else if (input instanceof IntegerField integer) {
            return outOps.createInt(integer.getInt());
        } else if (input instanceof FloatField flt) {
            return outOps.createFloat(flt.getFloat());
        } else if (input == null) {
            return outOps.empty();
        } else {
            throw new RuntimeException("Unknown widget type!");
        }
    }

    @Override
    public DataResult<Number> getNumberValue(Widget input) {
        if (input instanceof IntegerField integer) {
            return DataResult.success(integer.getInt());
        } else if (input instanceof FloatField flt) {
            return DataResult.success(flt.getFloat());
        } else if (input instanceof ToggleButton toggle) {
            return DataResult.success(toggle.isPressed() ? 1 : 0);
        } else if (input instanceof TextField field) {
            try {
                return DataResult.success(Integer.parseInt(field.getText()));
            } catch (NumberFormatException e) {
                try {
                    return DataResult.success(Float.parseFloat(field.getText()));
                } catch (NumberFormatException e2) {
                    return DataResult.error(() -> "Not a number: " + field.getText());
                }
            }
        } else {
            return DataResult.error(() -> "Not a number: " + input);
        }
    }

    @Override
    public Widget createNumeric(Number i) {
        return new IntegerField().integer(i.intValue());
    }

    @Override
    public DataResult<String> getStringValue(Widget input) {
        if (input instanceof TextField field) {
            return DataResult.success(field.getText());
        } else if (input instanceof ToggleButton toggle) {
            return DataResult.success(toggle.isPressed() ? "true" : "false");
        } else if (input instanceof IntegerField integer) {
            return DataResult.success(String.valueOf(integer.getInt()));
        } else if (input instanceof FloatField flt) {
            return DataResult.success(String.valueOf(flt.getFloat()));
        } else {
            return DataResult.error(() -> "Not a string: " + input);
        }
    }

    @Override
    public Widget createString(String value) {
        return new TextField().text(value);
    }

    @Override
    public DataResult<Widget> mergeToList(Widget list, Widget value) {
        return null;
    }

    @Override
    public DataResult<Widget> mergeToMap(Widget map, Widget key, Widget value) {
        return null;
    }

    @Override
    public DataResult<Stream<Pair<Widget, Widget>>> getMapValues(Widget input) {
        return null;
    }

    @Override
    public Widget createMap(Stream<Pair<Widget, Widget>> map) {
        return null;
    }

    @Override
    public DataResult<Stream<Widget>> getStream(Widget input) {
        return null;
    }

    @Override
    public Widget createList(Stream<Widget> input) {
        return null;
    }

    @Override
    public Widget remove(Widget input, String key) {
        return null;
    }
}
