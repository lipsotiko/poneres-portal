import type { Meta, StoryObj } from '@nuxtjs/storybook'

import DatePicker from '../DatePicker.vue'

const meta = {
  component: DatePicker,
  title: 'Components/DatePicker',
} satisfies Meta<typeof DatePicker>;

export default meta;
type Story = StoryObj<typeof meta>;
 
export const Primary: Story = {
  args: {
  }
};
