import type { Meta, StoryObj } from "@nuxtjs/storybook";

import OnboardingStatus from "../OnboardingStatus.vue";

const meta = {
  component: OnboardingStatus,
  title: "Components/OnboardingStatus",
} satisfies Meta<typeof OnboardingStatus>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    values: {
    },
  },
};
