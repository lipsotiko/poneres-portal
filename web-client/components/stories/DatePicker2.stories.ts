import type { Meta, StoryObj } from "@nuxtjs/storybook";
import { CalendarDate } from "@internationalized/date";

import DatePicker2 from "../DatePicker2.vue";

const meta = {
  component: DatePicker2,
  title: "Components/DatePicker2",
} satisfies Meta<typeof DatePicker2>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {},
};

export const PrimaryWithDate: Story = {
  args: {
    defaultValue: new CalendarDate(1988, 4, 28),
  },
};
