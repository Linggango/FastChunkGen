package com.misanthropy.fastchunkgen.client.embeddium;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;
import me.jellysquid.mods.sodium.client.gui.options.OptionFlag;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.network.chat.Component;

class OptionFactory {

    private static Component name(String key) {
        return Component.translatableWithFallback("fastchunkgen.option." + key, OptionText.name(key));
    }

    private static Component tooltip(String key) {
        return Component.translatableWithFallback("fastchunkgen.option." + key + ".tooltip", OptionText.tooltip(key));
    }

    static OptionImpl<Void, Boolean> bool(String key, boolean fallback) {
        return OptionImpl.createBuilder(boolean.class, OptionStore.INSTANCE)
                .setName(name(key))
                .setTooltip(tooltip(key))
                .setControl(TickBoxControl::new)
                .setBinding((data, value) -> ConfigSystem.write(key, value),
                        data -> ConfigSystem.readBoolean(key, fallback))
                .setFlags(OptionFlag.REQUIRES_GAME_RESTART)
                .build();
    }

    static OptionImpl<Void, Integer> sliderDisabledAtZero(String key, long fallback, int max, int step, ControlValueFormatter formatter) {
        return OptionImpl.createBuilder(int.class, OptionStore.INSTANCE)
                .setName(name(key))
                .setTooltip(tooltip(key))
                .setControl(option -> new SliderControl(option, 0, max, step, formatter))
                .setBinding((data, value) -> ConfigSystem.write(key, value == 0 ? -1L : value.longValue()),
                        data -> {
                            final long stored = ConfigSystem.readLong(key, fallback);
                            if (stored < 0) return 0;
                            return (int) Math.min(max, stored);
                        })
                .setFlags(OptionFlag.REQUIRES_GAME_RESTART)
                .build();
    }

    static OptionImpl<Void, Integer> slider(String key, long fallback, int min, int max, int step, ControlValueFormatter formatter) {
        if ((max - min) % step != 0) {
            throw new IllegalArgumentException("Slider range for " + key + " is not divisible by its step");
        }
        return OptionImpl.createBuilder(int.class, OptionStore.INSTANCE)
                .setName(name(key))
                .setTooltip(tooltip(key))
                .setControl(option -> new SliderControl(option, min, max, step, formatter))
                .setBinding((data, value) -> ConfigSystem.write(key, value.longValue()),
                        data -> (int) Math.min(max, Math.max(min, ConfigSystem.readLong(key, fallback))))
                .setFlags(OptionFlag.REQUIRES_GAME_RESTART)
                .build();
    }

    static <E extends Enum<E>> OptionImpl<Void, E> cycling(String key, Class<E> type, E fallback, Component[] labels) {
        return OptionImpl.createBuilder(type, OptionStore.INSTANCE)
                .setName(name(key))
                .setTooltip(tooltip(key))
                .setControl(option -> new CyclingControl<>(option, type, labels))
                .setBinding((data, value) -> ConfigSystem.write(key, value.name()),
                        data -> {
                            try {
                                return Enum.valueOf(type, ConfigSystem.readString(key, fallback.name()));
                            } catch (IllegalArgumentException e) {
                                return fallback;
                            }
                        })
                .setFlags(OptionFlag.REQUIRES_GAME_RESTART)
                .build();
    }
}
