import type { Meta, StoryObj } from '@nuxtjs/storybook'

import { Button } from '../ui/button'

const meta = {
  component: Button,
  title: 'Components/Button',
} satisfies Meta<typeof Button>;

export default meta;
type Story = StoryObj<typeof meta>;
 
export const Primary: Story = {
  args: {
    default: 'Hello',
  }
};

export const Secondary: Story = {
  args: {
    default: 'Hello',
    variant: 'secondary',
  }
};

export const Destructive: Story = {
  args: {
    default: 'Hello',
    variant: 'destructive',
  }
};

export const Outline: Story = {
  args: {
    default: 'Hello',
    variant: 'outline',
  }
};

export const Ghost: Story = {
  args: {
    default: 'Hello',
    variant: 'ghost',
  }
};

export const Link: Story = {
  args: {
    default: 'Hello',
    variant: 'link',
  },
};
