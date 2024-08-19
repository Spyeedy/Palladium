package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.ScoreboardUtil;
import net.threetag.palladium.util.context.DataContext;

import java.util.List;

public class ObjectiveScoreTextureVariable extends AbstractIntegerTextureVariable {

    private final String objectiveName;

    public ObjectiveScoreTextureVariable(String objectiveName, List<Pair<Operation, Integer>> operations) {
        super(operations);
        this.objectiveName = objectiveName;
    }

    @Override
    public int getNumber(DataContext context) {
        var entity = context.getEntity();
        return ScoreboardUtil.getScore(entity, this.objectiveName);
    }

    public static class Serializer implements TextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new ObjectiveScoreTextureVariable(
                    GsonHelper.getAsString(json, "objective"),
                    AbstractIntegerTextureVariable.parseOperations(json));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns the value of a score of an entity in the scoreboard for the given objective. IF YOU USE THIS, MAKE A 'tracked_score.json' AND PUT THE OBJECTIVE NAME IN IT, MORE ON THE WIKI!";
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Objective Score");

            builder.addProperty("objective", String.class)
                    .description("Name of the objective.")
                    .required().exampleJson(new JsonPrimitive("objective_name"));

            AbstractIntegerTextureVariable.addDocumentationFields(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("objective_score");
        }
    }
}
