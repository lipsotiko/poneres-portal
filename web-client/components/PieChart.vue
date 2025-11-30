<script setup lang="ts">
import type { ChartConfig } from "@/components/ui/chart";

import { Donut } from "@unovis/ts";
import { VisDonut, VisSingleContainer } from "@unovis/vue";
import { TrendingUp } from "lucide-vue-next";
import { computed } from "vue";
import { ChartContainer, ChartTooltip, ChartTooltipContent, componentToString } from "@/components/ui/chart";

const { matchScore } = defineProps(["matchScore"]);

const chartData = [
  { browser: "match", score: matchScore },
  { browser: "no_match", score: 100 - matchScore },
];
type Data = (typeof chartData)[number];

const chartConfig = {
  match: {
    color: "var(--chart-2)",
  },
  no_match: {
    color: "var(--vis-donut-background-color)",
  },
} satisfies ChartConfig;
</script>

<template>
  <ChartContainer
    :config="chartConfig"
    class="mx-auto aspect-square max-h-[250px]"
    :style="{
      '--vis-donut-central-label-font-size': 'var(--text-3xl)',
      '--vis-donut-central-label-font-weight': 'var(--font-weight-bold)',
      '--vis-donut-central-label-text-color': 'var(--foreground)',
      '--vis-donut-central-sub-label-text-color': 'var(--muted-foreground)',
    }"
  >
    <VisSingleContainer :data="chartData" :margin="{ top: 30, bottom: 30 }">
      <VisDonut
        :value="(d: Data) => d.score"
        :color="(d: Data) => chartConfig[d.browser as keyof typeof chartConfig].color"
        :arc-width="10"
        :central-label="matchScore"
      />
      <!-- <ChartTooltip
        :triggers="{
          [Donut.selectors.segment]: componentToString(chartConfig, ChartTooltipContent, { hideLabel: true })!,
        }"
      /> -->
    </VisSingleContainer>
  </ChartContainer>
</template>
