import type { Meta, StoryObj } from '@nuxtjs/storybook'

import CheckboxGroup from '../CheckboxGroup.vue'

const meta = {
  component: CheckboxGroup,
  title: 'Components/CheckboxGroup',
} satisfies Meta<typeof CheckboxGroup>;

export default meta;
type Story = StoryObj<typeof meta>;
 
export const Primary: Story = {
  args: {
    modelValue: {
      apple: true
    },
    options: [
        { value: "apple", label: "Apple" },
        { value: "peach", label: "Peach" },
        { value: "melon", label: "Melon" },
      ]
  }
};
