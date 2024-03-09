package com.mcjty.lostedit.compat;

import mcjty.lostcities.api.ILostCities;
import net.neoforged.fml.InterModComms;

import javax.annotation.Nullable;
import java.util.function.Function;

public class LostCitiesCompat
{
    public static ILostCities lostCities;

    public static void setupLostCities() {
        InterModComms.sendTo(ILostCities.LOSTCITIES, ILostCities.GET_LOST_CITIES, GetLostCities::new);
    }

    public static class GetLostCities implements Function<ILostCities, Void> {
        @Nullable
        @Override
        public Void apply(ILostCities lc) {
            lostCities = lc;
            return null;
        }
    }
}
